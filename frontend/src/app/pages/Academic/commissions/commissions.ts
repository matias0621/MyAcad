import { Component, NgModule } from '@angular/core';
import Commission from '../../../Models/Commission/commission';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommissionService } from '../../../Services/Commission/commission-service';
import { ProgramService } from '../../../Services/program-service';
import Program from '../../../Models/Program/Program';
import { NotificationService } from '../../../Services/notification/notification.service';

@Component({
  selector: 'app-commissions',
  imports: [ReactiveFormsModule, FormsModule],
  templateUrl: './commissions.html',
  styleUrl: './commissions.css'
})
export class Commissions {
  programs: Program[] = [];
  commissions !: Commission[];
  allCommissions!: Commission[];
  form!: FormGroup;
  filter: string = '';
  commissionId: number = 0;
  modalText = "Agregar";


  constructor(
    private fb: FormBuilder,
    private service: CommissionService,
    private pService: ProgramService,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    this.getCommissions();
    this.getPrograms();

    this.form = this.fb.group({
      number: ['', [Validators.required, Validators.min(0), Validators.pattern(/^[0-9]+$/)]],
      capacity: ['', [Validators.required, Validators.min(0), Validators.pattern(/^[0-9]+$/)]],
      program: ['', [Validators.required]],
      // envia las listas vacias para que no de error, en las listas se cargan materias y alumnos desde otras interfaces
      subjectIds: [[]],
      studentIds: [[]]
    });
  }

  getCommissions() {
    this.service.getCommissions().subscribe({
      next: (data) => {
        // Guarda las comisiones filtradas
        this.commissions = data;
        // Guarda todas las comisiones
        this.allCommissions = data;
      },
      error: (error) => { console.error(error) }
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

  OnSubmit() {
    if (this.commissionId != 0) {
      const commissionJson = {
        id: this.commissionId,
        active: true,
        ...this.form.value
      }
      this.service.putCommission(commissionJson).subscribe({
        next: (data) => {
          this.notificationService.success('Comision modificada exitosamente');
          this.form.reset();
          this.commissionId = 0;
          this.getCommissions();
        },
        error: (error) => {
          this.notificationService.error('Error al modificar comisión');
          console.error(error)
        }
      })
    } else {
      this.service.postCommission(this.form.value).subscribe({
        next: (data) => {
          this.notificationService.success('Comision agregada exitosamente');
          this.form.reset();
          this.commissionId = 0;
          this.getCommissions();
        },
        error: (error) => {
          this.notificationService.error('Error al agregar comisión');
          console.error(error)
        }
      })
    }
  }

  // Eliminar comision
  deleteCommission(id: number) {
    this.service.deleteCommission(id).subscribe({
      next: (data) => {
        console.log('Comision eliminada exitosamente');
        this.getCommissions();
      },
      error: (error) => { console.error(error) }
    })
  }

  // BAJA DEFINITIVA
  definitiveDeleteCommission(id: number) {
    this.notificationService.confirm(
      '¿Estás seguro de que deseas eliminar permanentemente esta comisión?',
      'Confirmar eliminación definitiva',
      'Eliminar',
      'Cancelar'
    ).then((confirmed) => {
      if (confirmed) {
        this.service.definitiveDeleteCommission(id).subscribe({
          next: (data) => {
            this.notificationService.success('Comisión eliminada exitosamente');
            this.getCommissions();
          },
          error: (error) => {
            this.notificationService.error('Error al eliminar la comisión. Por favor, intenta nuevamente', true);
          }
        });
      }
    });
  }

  addCommission() {
    this.modalText = "Agregar"
    this.commissionId = 0;
    this.form.reset();
  }
  // Editar comision
  modifyCommission(commission: Commission) {
    this.modalText = "Editar"
    this.commissionId = commission.id;
    this.form.patchValue(commission)
  }

  filterCommissions() {
    if (this.filter === '') {
      this.getCommissions();
    } else if (this.filter === "Activas") {
      this.commissions = this.allCommissions.filter(
        c => c.active === true
      )
    } else if (this.filter === "Inactivas") {
      this.commissions = this.allCommissions.filter(
        c => c.active === false
      )
    } else {
      this.service.getByProgram(this.filter).subscribe({
        next: (data) => { this.commissions = data },
        error: (error) => { console.error(error) }
      })
    }
  }

  viewDisabled(commission: Commission) {
    this.notificationService.confirm(
      `¿Deseas activar la comisión ${commission.number} de ${commission.program}?`,
      'Confirmar activación',
      'Activar',
      'Cancelar'
    ).then((confirmed) => {
      if (confirmed) {
        const updatedItem = { ...commission, active: true };
        this.service.putCommission(updatedItem).subscribe({
          next: (response) => {
            this.notificationService.success(`Comisión ${commission.number} de ${commission.program} activada exitosamente`);
            this.getCommissions();
          },
          error: (error) => {
            this.notificationService.error('Error al activar. Por favor, intenta nuevamente', true);
          }
        });
      }
    });
  }

  cleanForm() {
    this.form.reset();
  }
}
