import { Component, NgModule } from '@angular/core';
import Commission from '../../../Models/Commission/commission';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CommissionService } from '../../../Services/Commission/commission-service';
import { ProgramService } from '../../../Services/program-service';
import Program from '../../../Models/Program/Program';
import { NotificationService } from '../../../Services/notification/notification.service';
import { SubjectsService } from '../../../Services/Subjects/subjects-service';
import Subjects from '../../../Models/Subjects/Subjects';
import { UserService } from '../../../Services/Users/user-service';
import Student from '../../../Models/Users/Student';
import Teacher from '../../../Models/Users/Teachers';

@Component({
  selector: 'app-commissions',
  imports: [ReactiveFormsModule, FormsModule],
  templateUrl: './commissions.html',
  styleUrl: './commissions.css',
})
export class Commissions {
  programs: Program[] = [];
  commissions!: Commission[];
  allCommissions!: Commission[];
  subjectsList!: Subjects[];
  form!: FormGroup;
  filter: string = '';
  commissionId: number = 0;
  modalText = 'Agregar';
  selectedSubjectsIds: number[] = [];

  //Detalles
  selectedStudents !: Student[]
  selectedTeachers !: Teacher[];
  selectedCommission !: Commission;
  detailText !: String;
  detailText2 !: String;
  // Paginación
  totalPages: number = 0;
  currentPage: number = 0;

  constructor(
    private fb: FormBuilder,
    private service: CommissionService,
    private pService: ProgramService,
    private sService: SubjectsService,
    private uService: UserService,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    this.getCommissions();
    this.getPrograms();
    this.getSubjects()

    this.form = this.fb.group({
      number: ['', [Validators.required, Validators.min(0), Validators.pattern(/^[0-9]+$/)]],
      capacity: ['', [Validators.required, Validators.min(0), Validators.pattern(/^[0-9]+$/)]],
      program: ['', [Validators.required]],
      // envia las listas vacias para que no de error, en las listas se cargan materias y alumnos desde otras interfaces
      subjectIds: [[]],
      studentIds: [[]],
    });
  }

  getCommissions(page: number = 0, size: number = 10) {
    this.service.getCommissionsPaginated(page, size).subscribe({
      next: (data) => {
        // Guarda las comisiones filtradas
        this.commissions = data.content;
        // Guarda todas las comisiones
        this.allCommissions = data.content;
        console.log(data.content)
        // Paginación
        this.totalPages = data.totalPages;
        this.currentPage = data.number;
      },
      error: (error) => {
        console.error(error);
      },
    });
  }

  getSubjects() {
    this.sService.getAllSubject().subscribe({
      next: (res) => {
        this.subjectsList = res
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
      error: (error) => {
        console.error(error);
      },
    });
  }

  // OBTENER ALUMNOS DE UNA COMISION  
  getStudents(commissionId: number) {
    this.uService.getUsersByCommission(commissionId, 'students').subscribe({
      next: (data) => {
        this.detailText = "Alumnos"
        this.detailText2 = "alumnos"
        this.selectedStudents = data;
      },
      error: (error) => {

        console.error(error);
      }
    });
  }
  // OBTENER PROFESORES DE UNA COMISION  
  getTeachers(commissionId: number) {
    this.uService.getUsersByCommission(commissionId, 'teachers').subscribe({
      next: (data) => {
        this.detailText = "Profesores"
        this.detailText2 = "profesores"
        this.selectedTeachers = data;
      },
      error: (error) => {

        console.error(error);
      }
    });
  }

  OnSubmit() {
    if (this.form.invalid) {
      this.notificationService.warning('Formulario inválido. Por favor, complete todos los campos correctamente.');
      this.form.markAllAsTouched();
      return;
    }

    if (this.commissionId != 0) {
      const commissionJson = {
        id: this.commissionId,
        active: true,
        ...this.form.value,
      };
      this.service.putCommission(commissionJson).subscribe({
        next: (data) => {
          this.notificationService.success('Comision modificada exitosamente');
          this.form.reset();
          this.commissionId = 0;
          this.getCommissions();
        },
        error: (error) => {
          this.notificationService.error(error.error, true);
          console.error(error);
        },
      });
    } else {
      const commissionJson = {
        active: true,
        ...this.form.value,
      };
      this.service.postCommission(commissionJson).subscribe({
        next: (data) => {
          this.notificationService.success('Comision agregada exitosamente');
          this.form.reset();
          this.commissionId = 0;
          this.getCommissions();
        },
        error: (error) => {
          this.notificationService.error(error.error, true);
          console.error(error);
        },
      });
    }
  }

  // Eliminar comision
  deleteCommission(id: number) {
    this.notificationService
      .confirm(
        '¿Estás seguro de que deseas eliminar esta comisión?',
        'Confirmar eliminación',
        'Eliminar',
        'Cancelar'
      )
      .then((confirmed) => {
        if (confirmed) {
          this.service.deleteCommission(id).subscribe({
            next: (data) => {
              this.notificationService.success('Comisión eliminada exitosamente');
              this.getCommissions();
            },
            error: (error) => {
              this.notificationService.error(
                'Error al eliminar la comisión. Por favor, intenta nuevamente',
                true
              );
            },
          });
        }
      });
  }

  addToCommission(subjectsId: number) {
    if (!this.commissionId) {
      this.notificationService.error('Seleccioná una comisión antes de asignar materias', true);
      return;
    }

    const index = this.selectedSubjectsIds.indexOf(subjectsId);
    const removing = index > -1;

    if (removing) {
      this.selectedSubjectsIds.splice(index, 1);
      this.service.removeSubjectFromCommission(this.commissionId, subjectsId).subscribe({
        next: () => {
          this.notificationService.success('Se eliminó la materia de la comisión');
          this.getCommissions();
        },
        error: (err) => {
          this.notificationService.error('Hubo un error al eliminar la materia', true);
          console.log(err);
          this.selectedSubjectsIds.push(subjectsId);
        },
      });
    } else {
      this.selectedSubjectsIds.push(subjectsId);
      this.service.addSubjectsToCommission(this.commissionId, subjectsId).subscribe({
        next: () => {
          this.notificationService.success('Se añadió la materia a la comisión y carrera');
          this.getCommissions();
        },
        error: (err) => {
          this.notificationService.error('Hubo un error al añadir la materia', true);
          console.log(err);
          const rollbackIndex = this.selectedSubjectsIds.indexOf(subjectsId);
          if (rollbackIndex > -1) {
            this.selectedSubjectsIds.splice(rollbackIndex, 1);
          }
        },
      });
    }
  }

  // BAJA DEFINITIVA
  definitiveDeleteCommission(id: number) {
    this.notificationService
      .confirm(
        '¿Estás seguro de que deseas eliminar permanentemente esta comisión?',
        'Confirmar eliminación definitiva',
        'Eliminar',
        'Cancelar'
      )
      .then((confirmed) => {
        if (confirmed) {
          this.service.definitiveDeleteCommission(id).subscribe({
            next: (data) => {
              this.notificationService.success('Comisión eliminada exitosamente');
              this.getCommissions();
            },
            error: (error) => {
              this.notificationService.error(
                'Error al eliminar la comisión. Por favor, intenta nuevamente',
                true
              );
            },
          });
        }
      });
  }

  setCommisionId(id: number) {
    this.commissionId = id
    let ids: number[] = []

    this.service.getById(id).subscribe({
      next: (res) => {
        res.subjects.forEach((s) => ids.push(s.id))
        this.selectedSubjectsIds = ids
      },
      error: (err) => {
        console.log(err)
      }
    })



  }

  addCommission() {
    this.modalText = 'Agregar';
    this.commissionId = 0;
    this.form.reset();
  }
  // Editar comision
  modifyCommission(commission: Commission) {
    this.modalText = 'Editar';
    this.commissionId = commission.id;
    this.form.patchValue(commission);
  }

  filterCommissions() {
    if (this.filter === '') {
      this.getCommissions();
    } else if (this.filter === 'Activas') {
      this.commissions = this.allCommissions.filter((c) => c.active === true);
    } else if (this.filter === 'Inactivas') {
      this.commissions = this.allCommissions.filter((c) => c.active === false);
    } else {
      this.service.getByProgram(this.filter).subscribe({
        next: (data) => {
          this.commissions = data;
        },
        error: (error) => {
          console.error(error);
        },
      });
    }
  }

  viewDisabled(commission: Commission) {
    this.notificationService
      .confirm(
        `¿Deseas activar la comisión ${commission.number} de ${commission.program}?`,
        'Confirmar activación',
        'Activar',
        'Cancelar'
      )
      .then((confirmed) => {
        if (confirmed) {
          const updatedItem = { ...commission, active: true };
          this.service.putCommission(updatedItem).subscribe({
            next: (response) => {
              this.notificationService.success(
                `Comisión ${commission.number} de ${commission.program} activada exitosamente`
              );
              this.getCommissions();
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

  cleanForm() {
    this.form.reset();
  }
}
