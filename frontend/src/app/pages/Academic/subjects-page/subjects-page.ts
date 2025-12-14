import { NotificationService } from './../../../Services/notification/notification.service';
import { Component } from '@angular/core';
import Commission from '../../../Models/Commission/commission';
import Subjects from '../../../Models/Subjects/Subjects';
import { SubjectsService } from '../../../Services/Subjects/subjects-service';
import { CommissionService } from '../../../Services/Commission/commission-service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProgramService } from '../../../Services/Program/program-service';
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
  private allSubjectsLoaded = false;
  private showingAllSubjects = true;
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
    this.loadAllSubjects();

    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(30)]],
      description: ['', [Validators.required, Validators.maxLength(300)]],
      semesters: ['', [Validators.required, Validators.maxLength(50)]],
      acacademicStatus: ['', [Validators.required]],
      program: ['', [Validators.required]]
    });
  }

  getAllSubject(page: number = 0, size: number = 10) {
    this.subjectService.getAllSubjectPaginated(page, size).subscribe({
      next: (res) => {
        console.log(res.content)
        this.subjects = res.content;
        this.totalPages = res.totalPages;
        this.currentPage = res.number;
        this.loadAllSubjects(this.filter !== '' || this.showingAllSubjects);
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  private loadAllSubjects(applyFilterAfterLoad: boolean = false) {
    this.allSubjectsLoaded = false;
    this.subjectService.getAllSubject().subscribe({
      next: (subjects) => {
        this.allSubjects = subjects;
        this.allSubjectsLoaded = true;
        if (applyFilterAfterLoad) {
          this.applyFilter();
        }
      },
      error: (error) => {
        console.error(error);
      }
    });
  }


  prerequisite(subjects: Subjects) {
    this.subjectId = subjects.id;
    this.selectedSubject = subjects;
    if (!this.selectedSubject.prerequisites) {
      this.selectedSubject.prerequisites = [];
    }

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
      next: () => {
        this.notificationService.success("Se agrego la correlativa");
        if (this.selectedSubject?.prerequisites && !this.selectedSubject.prerequisites.some((p) => p.id === subjectPrerequisiteId)) {
          const prerequisite = this.listPrerequisite.find((p) => p.id === subjectPrerequisiteId);
          if (prerequisite) {
            this.selectedSubject.prerequisites = [...this.selectedSubject.prerequisites, prerequisite];
          }
        }
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
        this.notificationService.success("Se elimino la correlativa");
        if (this.selectedSubject?.prerequisites) {
          this.selectedSubject.prerequisites = this.selectedSubject.prerequisites.filter((p) => p.id !== subjectPrerequisiteId);
        }
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
            this.notificationService.error('Esta  materia tiene correlativas', true);
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
            const errorMessage = this.getErrorMessage(error, 'materia');
            this.notificationService.error(errorMessage, true);
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
      this.showingAllSubjects = true;
    } else {
      this.showingAllSubjects = false;
    }

    if (!this.allSubjectsLoaded) {
      this.loadAllSubjects(true);
      return;
    }

    this.applyFilter();
  }

  private applyFilter(): void {
    if (!this.allSubjects) {
      this.subjects = [];
      return;
    }

    if (this.filter === '') {
      this.subjects = [...this.allSubjects];
      this.totalPages = 1;
      this.currentPage = 0;
      return;
    }

    if (this.filter === 'Activas') {
      this.subjects = this.allSubjects.filter((s) => s.subjectActive);
      this.totalPages = 1;
      this.currentPage = 0;
      return;
    }

    if (this.filter === 'Inactivas') {
      this.subjects = this.allSubjects.filter((s) => !s.subjectActive);
      this.totalPages = 1;
      this.currentPage = 0;
      return;
    }

    this.subjects = this.allSubjects.filter((s) => s.program === this.filter);
    this.totalPages = 1;
    this.currentPage = 0;
  }

  isPrerequisite(subjectId: number): boolean {
    return !!this.selectedSubject?.prerequisites?.some((p) => p.id === subjectId);
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

  private getErrorMessage(error: any, entityType: string): string {
    let errorMessage = `Error al eliminar la ${entityType}. Por favor, intenta nuevamente`;
    
    if (error?.error) {
      if (typeof error.error === 'string') {
        errorMessage = error.error;
      } else if (error.error?.message) {
        errorMessage = error.error.message;
      } else if (error.error?.error) {
        errorMessage = error.error.error;
      }
    } else if (error?.message) {
      errorMessage = error.message;
    }
    
    if (errorMessage.includes('Unable to find')) {
      if (errorMessage.includes('Subject')) {
        errorMessage = 'No se puede eliminar porque hay un problema con las materias asociadas. Verifica que todas las relaciones estén correctas.';
      } else if (errorMessage.includes('Commission')) {
        errorMessage = 'No se puede eliminar porque hay un problema con las comisiones asociadas.';
      } else if (errorMessage.includes('Teacher')) {
        errorMessage = 'No se puede eliminar porque hay un problema con los profesores asociados.';
      } else {
        errorMessage = 'No se puede eliminar porque hay relaciones asociadas que no se pueden procesar.';
      }
    }
    
    if (errorMessage.includes('with id 0')) {
      errorMessage = 'No se puede eliminar porque hay datos incompletos o inválidos en las relaciones asociadas.';
    }
    
    if (errorMessage.includes('foreign key constraint') || errorMessage.includes('constraint')) {
      errorMessage = 'No se puede eliminar porque está asociado a otros registros en el sistema.';
    }
    
    return errorMessage;
  }
}
