import { Component, NgModule } from '@angular/core';
import Commission from '../../../Models/Commission/commission';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommissionService } from '../../../Services/Commission/commission-service';
import { CareerService } from '../../../Services/CareerService/career-service';

@Component({
  selector: 'app-commissions',
  imports: [ReactiveFormsModule, FormsModule],
  templateUrl: './commissions.html',
  styleUrl: './commissions.css'
})
export class Commissions {
  programs: any[] = [];
  commissions !: Commission[];
  allCommissions!: Commission[];
  form!: FormGroup;
  filter: string = '';
  commissionId: number = 0;
  modalText = "Agregar";


  constructor(
    private fb: FormBuilder,
    private service: CommissionService,
    private pService: CareerService
  ) { }

  ngOnInit(): void {
    this.getCommissions();
    this.getPrograms();

    this.form = this.fb.group({
      number: ['', [Validators.required, Validators.min(0), Validators.pattern(/^[0-9]+$/)]],
      capacity: ['', [Validators.required, Validators.min(0), Validators.pattern(/^[0-9]+$/)]],
      program: ['', [Validators.required]],
      active: [true],
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
    //Cargamos las tecnicaturas al array de carrras
    this.pService.getCareers("technicals").subscribe({
      next: (data) => {
        data.forEach(element => {
          this.programs.push(element)
        })
      },
      error: (error) => { console.error(error) }
    })
    //Cargamos las ingenierias al array de carrras
    this.pService.getCareers("careers").subscribe({
      next: (data) => {
        data.forEach(element => {
          this.programs.push(element)
        })
      },
      error: (error) => { console.error(error) }
    })
    //Cargamos los cursos al array de carrras
    this.pService.getCareers("courses").subscribe({
      next: (data) => {
        data.forEach(element => {
          this.programs.push(element)
        })
      },
      error: (error) => { console.error(error) }
    })
  }

  OnSubmit() {
    if (this.commissionId != 0) {
      const commissionJson = {
        id: this.commissionId,
        ...this.form.value
      }
      this.service.putCommission(commissionJson).subscribe({
        next: (data) => {
          console.log('Comision editada exitosamente');
          this.form.reset();
          this.commissionId = 0;
          this.getCommissions();
        },
        error: (error) => { console.error(error) }
      })
    } else {
      this.service.postCommission(this.form.value).subscribe({
        next: (data) => {
          console.log('Comision agregada exitosamente');
          this.form.reset();
          this.commissionId = 0;
          this.getCommissions();
        },
        error: (error) => { console.error(error) }
      })
    }
  }

  getProgramById() {
    this.pService.getCareers("courses").subscribe({
      next: (data) => {
        data.forEach(element => {
          this.programs.push(element)
        })
      },
      error: (error) => { console.error(error) }
    })
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



  cleanForm() {
    this.form.reset();
  }
}
