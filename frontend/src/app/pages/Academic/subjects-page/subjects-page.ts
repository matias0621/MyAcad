import { NotificationService } from './../../../Services/notification/notification.service';
import { Component } from '@angular/core';
import Commission from '../../../Models/Commission/commission';
import Subjects from '../../../Models/Subjects/Subjects';
import { SubjectsService } from '../../../Services/Subjects/subjects-service';
import { CommissionService } from '../../../Services/Commission/commission-service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProgramService } from '../../../Services/program-service';
import Program from '../../../Models/Program/Program';

@Component({
  selector: 'app-subjects-page',
  imports: [ReactiveFormsModule, FormsModule],
  templateUrl: './subjects-page.html',
  styleUrl: './subjects-page.css'
})
export class SubjectsPage {
  modalText = "Agregar";
  form!: FormGroup;
  subjectId: number = 0;
  filter: string = '';
  listCommission: Commission[] = []
  programs: Program[] = [];
  subjects!: Subjects[];
  allSubjects !: Subjects[];
  listPrerequisite: Subjects[] = []
  selectedSubject ?: Subjects;
  // Paginación
  totalPages: number = 0;
  currentPage: number = 0;

  constructor(
    private fb: FormBuilder,
    public subjectService: SubjectsService,
    public commissionService: CommissionService,
    private pService: ProgramService,
    private notificationService: NotificationService
  ) {
  }

  ngOnInit(): void {
    this.getPrograms();
    this.getAllSubject();
    this.getAllCommission();

    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(30)]],
      description: ['', [Validators.required, Validators.maxLength(300)]],
      semesters: ['', [Validators.required, Validators.maxLength(50)]],
      // acacademicStatus: ['', [Validators.required]],
      program: ['', [Validators.required]]
    });
  }

  getAllSubject(page: number = 0, size: number = 10) {
    this.subjectService.getAllSubjectPaginated(page, size).subscribe({
      next: (res) => {
        console.log(res.content)
        this.subjects = res.content;
        this.allSubjects = res.content;
        this.totalPages = res.totalPages;
        this.currentPage = res.number;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }


  prerequisite(subjects: Subjects) {
    this.subjectId = subjects.id

    this.getAllBySemestraLessThanAndProgram(subjects.program, subjects.semesters)
  }

  getAllBySemestraLessThanAndProgram(program: string, semester: number) {
    this.subjectService.getAllSubjectWithSemesterLessThanAndProgram(semester, program).subscribe({
      next: (res) => {
        this.listPrerequisite = res
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  addPrerequisite(subjectPrerequisiteId: number) {
    this.subjectService.addPrerequisite(this.subjectId, subjectPrerequisiteId).subscribe({
      next: (res) => {
        this.notificationService.success("Se agrego la correlativa")
        this.getAllSubject()
      },
      error: (err) => {
        this.notificationService.error("Hubo un error, intente denuevo mas tarde", false)
      }
    })
  }

  deletePrerequisite(subjectPrerequisiteId: number) {
    this.subjectService.deletePrerequiste(this.subjectId, subjectPrerequisiteId).subscribe({
      next: (res) => {
        this.notificationService.success("Se elimino la correlativa")
        this.getAllSubject()
      },
      error: (err) => {
        this.notificationService.error("Hubo un error, intente denuevo mas tarde", false)
      }
    })
  }


  getAllCommission() {
    this.commissionService.getCommissions().subscribe({
      next: (res) => {
        this.listCommission = res
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  //OBTENER CARRERAS
  getPrograms() {
    this.pService.getPrograms().subscribe({
      next: (data) => {
        this.programs = data;
      },
      error: (error) => { console.error(error) }
    })
  }

  // BAJA LOGICA
  deleteSubject(id: number) {
    this.notificationService.confirm(
      '¿Estás seguro de que deseas eliminar esta materia?',
      'Confirmar eliminación',
      'Eliminar',
      'Cancelar'
    ).then((confirmed) => {
      if (confirmed) {
        this.subjectService.deleteSubject(id).subscribe({
          next: (data) => {
            this.notificationService.success('Materia eliminada exitosamente');
            this.getAllSubject();
          },
          error: (error) => {
            this.notificationService.error('Error al eliminar la materia. Por favor, intenta nuevamente', true);
          }
        });
      }
    });
  }

  // BAJA DEFINITIVA
  definitiveDeleteSubject(id: number) {
    this.notificationService.confirm(
      '¿Estás seguro de que deseas eliminar permanentemente esta materia?',
      'Confirmar eliminación definitiva',
      'Eliminar',
      'Cancelar'
    ).then((confirmed) => {
      if (confirmed) {
        this.subjectService.definitiveDeleteSubject(id).subscribe({
          next: (data) => {
            this.notificationService.success('Materia eliminada exitosamente');
            this.getAllSubject();
          },
          error: (error) => {
            this.notificationService.error('Error al eliminar la materia. Por favor, intenta nuevamente', true);
          }
        });
      }
    });
  }

  viewDisabled(subject: Subjects) {
    this.notificationService.confirm(
      `¿Deseas activar "${subject.name}"?`,
      'Confirmar activación',
      'Activar',
      'Cancelar'
    ).then((confirmed) => {
      if (confirmed) {
        const updatedItem = { ...subject, subjectActive: true };
        this.subjectService.putSubject(updatedItem).subscribe({
          next: (response) => {
            console.log(updatedItem);
            this.notificationService.success(`${subject.name} activado/a exitosamente`);
            this.getAllSubject();
          },
          error: (error) => {
            this.notificationService.error('Error al activar. Por favor, intenta nuevamente', true);
          }
        });
      }
    });
  }

  // Editar materia
  modifySubject(subject: Subjects) {
    this.modalText = "Editar"
    this.subjectId = subject.id;
    this.form.patchValue(subject)
  }

  // Agregar materia
  addSubject() {
    this.modalText = "Agregar"
    this.subjectId = 0;
    this.form.reset();
  }



  filterSubjects() {
    if (this.filter === '') {
      this.getAllSubject();
    } else if (this.filter === "Activas") {
      this.subjects = this.allSubjects.filter(
        s => s.subjectActive === true
      )
    } else if (this.filter === "Inactivas") {
      this.subjects = this.allSubjects.filter(
        s => s.subjectActive === false
      )
    } else {
      this.subjectService.getByProgram(this.filter).subscribe({
        next: (data) => { this.subjects = data },
        error: (error) => { console.error(error) }
      })
    }
  }

  // form
  OnSubmit() {

    if (this.form.invalid) {
      this.notificationService.warning('Formulario inválido. Por favor, complete todos los campos correctamente.');
      this.form.markAllAsTouched();
      return;
    }

    if (this.subjectId != 0) {
      const subjectJson = {
        id: this.subjectId,
        subjectActive: true,
        ...this.form.value
      }
      this.subjectService.putSubject(subjectJson).subscribe({
        next: (data) => {
          console.log('Materia editada exitosamente');
          this.form.reset();
          this.subjectId = 0;
          this.getAllSubject();
        },
        error: (error) => {
          this.notificationService.error(error.error, true);
          console.error(error)
        }
      })
    } else {
      const subjectJson = {
        academicStatus: 'INPROGRESS',
        prerequisites: [],
        ...this.form.value
      }

      this.subjectService.postSubject(subjectJson).subscribe({
        next: (data) => {
          console.log('Materia agregada exitosamente');
          this.form.reset();
          this.subjectId = 0;
          this.getAllSubject();
        },
        error: (error) => {
          this.notificationService.error(error.error, true);
          console.error(error)
        }
      })
    }
  }

  cleanForm() {
    this.form.reset();
  }
}
