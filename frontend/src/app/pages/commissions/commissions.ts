import { Component, NgModule } from '@angular/core';
import Commission from '../../Models/Commission/commission';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommissionService } from '../../Services/Commission/commission-service';
import { CareerService } from '../../Services/CareerService/career-service';

@Component({
  selector: 'app-commissions',
  imports: [ReactiveFormsModule, FormsModule],
  templateUrl: './commissions.html',
  styleUrl: './commissions.css'
})
export class Commissions {
  programs: any[] = [];
  commissions!: Commission[];
  formAdd!: FormGroup;
  formModify!: FormGroup;
  selectedProgram: string = '';
  commissionId: number = 0;

  constructor(
    private fb: FormBuilder,
    private service: CommissionService,
    private pService: CareerService
  ) { }

  ngOnInit(): void {
    this.getCommissions();
    this.getPrograms();

    this.formAdd = this.fb.group({
      number: ['', [Validators.required, Validators.min(0), Validators.pattern(/^[0-9]+$/)]],
      capacity: ['', [Validators.required, Validators.min(0), Validators.pattern(/^[0-9]+$/)]],
      program: ['', [Validators.required]],
      active: [true, [Validators.required]],
      // envia las listas vacias para que no de error, en las listas se cargan materias y alumnos desde otras interfaces
      subjectIds: [[]],
      studentIds: [[]]
    });

    this.formModify = this.fb.group({
      number: ['', [Validators.required, Validators.min(0), Validators.pattern(/^[0-9]+$/)]],
      capacity: ['', [Validators.required, Validators.min(0), Validators.pattern(/^[0-9]+$/)]],
      program: ['', [Validators.required]],
      active: [true, [Validators.required]],
      // envia las listas vacias para que no de error, en las listas se cargan materias y alumnos desde otras interfaces
      subjectIds: [[]],
      studentIds: [[]]
    });
  }

  getCommissions() {
    this.service.getCommissions().subscribe({
      next: (data) => { this.commissions = data },
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
      this.service.postCommission(this.formAdd.value).subscribe({
        next: (data) => {
          console.log('Comision agregada exitosamente');
          this.formAdd.reset();
          this.getCommissions();
        },
        error: (error) => { console.error(error) }
      })
    } else {
      this.service.putCommission(this.formAdd.value).subscribe({
        next: (data) => {
          console.log('Comision editada exitosamente');
          this.formModify.reset();
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
  // Editar comision
  modifyCommission(commission: Commission) {

  }

  filterByProgram() {
    if (this.selectedProgram === '') {
      this.getCommissions();
    } else {
      this.service.getByProgram(this.selectedProgram).subscribe({
        next: (data) => { this.commissions = data },
        error: (error) => { console.error(error) }
      })
    }
  }

  // loadData(careerData: any) {
  //   this.careerId = careerData.id

  //   const mappedData = {
  //     name: careerData.name,
  //     description: careerData.description,
  //     durationMonths: careerData.durationMonths,
  //     monthlyFee: careerData.monthlyFee,
  //     annualFee: careerData.annualFee,
  //     active: careerData.active
  //   }

  //   this.formPrograms.patchValue(mappedData)
  // }

  cleanAddForm() {
    this.formAdd.reset();
  }
}
