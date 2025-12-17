import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import Subjects from '../../../Models/Subjects/Subjects';
import Program from '../../../Models/Program/Program';
import { SubjectsService } from '../../../Services/Subjects/subjects-service';
import { NotificationService } from '../../../Services/notification/notification.service';
import { PostSubjectPrerequisite } from '../../../Models/SubjectPrerequisite/SubjectPrerequisite';
import { ProgramService } from '../../../Services/Program/program-service';
import { SubjectPrerequisiteService } from '../../../Services/subject-prerequisite-service';
import { SettingService } from '../../../Services/setting-service';

@Component({
  selector: 'app-subjects-page',
  imports: [ReactiveFormsModule, FormsModule],
  templateUrl: './subjects-page.html',
  styleUrl: './subjects-page.css',
})
export class SubjectsPage {
  modalText = 'Agregar';
  form!: FormGroup;

  subjects: Subjects[] = [];
  allSubjects: Subjects[] = [];
  programs: Program[] = [];

  selectedSubject?: Subjects;
  listPrerequisite: Subjects[] = [];
  evaluationsEnabled: boolean = false;

  subjectId = 0;

  /** estado seleccionado por materia correlativa */
  selectedStatus: { [subjectId: number]: string } = {};

  // Paginación
  totalPages: number = 0;
  currentPage: number = 0;
  private allSubjectsLoaded = false;
  private showingAllSubjects = true;


  filter = '';

  constructor(
    private fb: FormBuilder,
    private subjectService: SubjectsService,
    private subjectPrerequisiteService: SubjectPrerequisiteService,
    private programService: ProgramService,
    public settingService: SettingService,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      semesters: ['', Validators.required],
      program: ['', Validators.required],
    });

    this.getAllSubject();
    this.getPrograms();
    this.loadEvaluationSetting()
  }

  // ===================== SUBJECTS =====================

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
      },
    });
  }

  getAllSubject(page: number = 0, size: number = 10) {
    this.subjectService.getAllSubjectPaginated(page, size).subscribe({
      next: (res) => {
        this.subjects = res.content;
        this.allSubjects = res.content;

        this.totalPages = res.totalPages;
        this.currentPage = res.number;
      },
      error: (err) => console.error(err),
    });
  }

  getPrograms() {
    this.programService.getPrograms().subscribe({
      next: (res) => (this.programs = res),
    });
  }

  // ===================== PREREQUISITES =====================

  prerequisite(subject: Subjects) {
    this.selectedSubject = subject;
    this.subjectId = subject.id;

    if (!this.selectedSubject.prerequisites) {
      this.selectedSubject.prerequisites = [];
    }

    this.subjectService
      .getAllSubjectWithSemesterLessThanAndProgram(subject.semesters, subject.program)
      .subscribe({
        next: (res) => (this.listPrerequisite = res),
      });
  }

  isPrerequisite(subjectId: number): boolean {
    return !!this.selectedSubject?.prerequisites?.some((p) => p.prerequisite.id === subjectId);
  }

  addPrerequisite(prerequisiteId: number) {
    const status = this.selectedStatus[prerequisiteId];

    if (!status) {
      this.notificationService.warning('Seleccione si debe aprobar o promocionar');
      return;
    }

    const dto: PostSubjectPrerequisite = {
      subjectId: this.subjectId,
      prerequisiteId,
      requiredStatus: status,
    };

    this.subjectPrerequisiteService.create(dto).subscribe({
      next: () => {
        this.notificationService.success('Correlativa agregada');
        this.selectedStatus[prerequisiteId] = '';
        this.reloadPrerequisites();
      },
      error: (err) => {
        this.notificationService.error('Error al agregar correlativa', false);
        console.log(err)
      },
    });
  }



  deletePrerequisiteRelation(prerequisiteSubjectId: number) {
    if (!this.selectedSubject?.prerequisites) return;

    const relation = this.selectedSubject.prerequisites.find(
      p => p.prerequisite.id === prerequisiteSubjectId
    );

    if (!relation) return;

    this.subjectPrerequisiteService
      .delete(this.selectedSubject.id, relation.prerequisite.id)
      .subscribe({
        next: () => {
          this.notificationService.success('Correlativa eliminada');
          this.reloadPrerequisites();
        },
        error: () => {
          this.notificationService.error('Error al eliminar correlativa', false);
        }
      });
  }

  private reloadPrerequisites() {
    this.subjectPrerequisiteService.findBySubject(this.subjectId).subscribe({
      next: (res) => {
        if (this.selectedSubject) {
          this.selectedSubject.prerequisites = res;
        }
        this.getAllSubject();
      },
    });
  }

  // ===================== FORM =====================

  OnSubmit() {
    if (this.form.invalid) return;

    const payload = {
      ...this.form.value,
      subjectActive: true,
    };

    if (this.subjectId === 0) {
      this.subjectService.postSubject(payload as Subjects).subscribe({
        next: (res) => {
        this.notificationService.success('Materia creada');
        this.getAllSubject();
        this.form.reset();
        },
        error: (err) => {
          this.notificationService.error('Error al crear materia', true);
          console.log(err);
        }
      });
    } else {
      this.subjectService
        .putSubject({
          id: this.subjectId,
          ...payload,
        } as Subjects)
        .subscribe(() => {
          this.notificationService.success('Materia actualizada');
          this.getAllSubject();
          this.form.reset();
          this.subjectId = 0;
        });
    }
  }

  // ===================== UI =====================

  modifySubject(subject: Subjects) {
    this.modalText = 'Editar';
    this.subjectId = subject.id;
    this.form.patchValue(subject);
  }

  addSubject() {
    this.modalText = 'Agregar';
    this.subjectId = 0;
    this.form.reset();
  }

  cleanForm() {
    this.form.reset();
  }

  // BAJA LOGICA
  deleteSubject(id: number) {
    this.notificationService
      .confirm(
        '¿Estás seguro de que deseas eliminar esta materia?',
        'Confirmar eliminación',
        'Eliminar',
        'Cancelar'
      )
      .then((confirmed) => {
        if (confirmed) {
          this.subjectService.deleteSubject(id).subscribe({
            next: (data) => {
              this.notificationService.success('Materia eliminada exitosamente');
              this.getAllSubject();
            },
            error: (error) => {
              this.notificationService.error('Esta  materia tiene correlativas', true);
            },
          });
        }
      });
  }

  // BAJA DEFINITIVA
  definitiveDeleteSubject(id: number) {
    this.notificationService
      .confirm(
        '¿Estás seguro de que deseas eliminar permanentemente esta materia?',
        'Confirmar eliminación definitiva',
        'Eliminar',
        'Cancelar'
      )
      .then((confirmed) => {
        if (confirmed) {
          this.subjectService.definitiveDeleteSubject(id).subscribe({
            next: (data) => {
              this.notificationService.success('Materia eliminada exitosamente');
              this.getAllSubject();
            },
            error: (error) => {
              const errorMessage = this.getErrorMessage(error, 'materia');
              this.notificationService.error(errorMessage, true);
            },
          });
        }
      });
  }

  viewDisabled(subject: Subjects) {
    this.notificationService
      .confirm(`¿Deseas activar "${subject.name}"?`, 'Confirmar activación', 'Activar', 'Cancelar')
      .then((confirmed) => {
        if (confirmed) {
          const updatedItem = { ...subject, subjectActive: true };

          this.subjectService.putSubject(updatedItem).subscribe({
            next: (response) => {
              this.notificationService.success(`${subject.name} activado/a exitosamente`);
              this.getAllSubject();
            },
            error: (error) => {
              this.notificationService.error(
                'Error al activar. Por favor, intenta nuevamente',
                true
              );
            },
          });
        }
      });
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
        errorMessage =
          'No se puede eliminar porque hay un problema con las materias asociadas. Verifica que todas las relaciones estén correctas.';
      } else if (errorMessage.includes('Commission')) {
        errorMessage = 'No se puede eliminar porque hay un problema con las comisiones asociadas.';
      } else if (errorMessage.includes('Teacher')) {
        errorMessage = 'No se puede eliminar porque hay un problema con los profesores asociados.';
      } else {
        errorMessage =
          'No se puede eliminar porque hay relaciones asociadas que no se pueden procesar.';
      }
    }

    if (errorMessage.includes('with id 0')) {
      errorMessage =
        'No se puede eliminar porque hay datos incompletos o inválidos en las relaciones asociadas.';
    }

    if (errorMessage.includes('foreign key constraint') || errorMessage.includes('constraint')) {
      errorMessage = 'No se puede eliminar porque está asociado a otros registros en el sistema.';
    }

    return errorMessage;
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

  loadEvaluationSetting() {
    this.settingService.isCourseEvaluationEnabled().subscribe(value => {
      this.evaluationsEnabled = value;
    });
  }

  toggleEvaluations(event: Event) {
    const checked = (event.target as HTMLInputElement).checked;
    this.evaluationsEnabled = checked;

    this.settingService.setCourseEvaluationEnabled(checked).subscribe();
  }
}
