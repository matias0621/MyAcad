import { Component, OnDestroy, OnInit } from '@angular/core';
import { CareerService } from '../../Services/CareerService/career-service';
import { CommissionService } from '../../Services/Commission/commission-service';
import Career from '../../Models/Users/Careers/Career';
import Technical from '../../Models/Users/Careers/Technical';
import Course from '../../Models/Users/Careers/Course';
import Commission from '../../Models/Commission/commission';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import Subjects from '../../Models/Subjects/Subjects';
import { RegistrationStudent } from '../../Models/Users/Student';

@Component({
  selector: 'app-inscription-to-commission',
  imports: [ReactiveFormsModule],
  templateUrl: './inscription-to-commission.html',
  styleUrl: './inscription-to-commission.css'
})
export class InscriptionToCommission implements OnInit, OnDestroy {

  listCommission: Commission[] = []
  form!:FormGroup
  legajo!:FormControl
  com!:Commission
  sub!:Subjects

  constructor(public careerService:CareerService, 
    public commisionService:CommissionService){
      this.legajo = new FormControl("", Validators.required)
      this.form = new FormGroup({
        legajo:this.legajo
      })
    }

  ngOnInit(): void {
    this.getCommisionByNameProgram()
  }

  ngOnDestroy(): void {
    this.careerService.setCareerSelected(null)
  }

  getCommisionByNameProgram(){
    
    const program:string = this.careerService.getCareerSelected() ?? "career"; 

    this.commisionService.getByProgram(program).subscribe({
      next: (res) => {
        this.listCommission = res
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  addStudentToCommision(){

    const request:RegistrationStudent = {
      studentLegajo: this.legajo.value,
      subjectsId: this.sub.id
    }

    this.commisionService.registerStudentToCommissionByManager(this.com.id, request).subscribe({
      next: (res) => {
        alert("Se registro el alumno correctamente")
      },
      error: (err) => {
        alert("Hubo un error")
        console.log(err)
      }
    })
  }

  setCommissionAndSubject(com:Commission, s:Subjects){
     this.com = com
     this.sub = s
  }
}
