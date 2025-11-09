import { Component, OnDestroy, OnInit } from '@angular/core';
import { CareerService } from '../../Services/CareerService/career-service';
import { CommissionService } from '../../Services/Commission/commission-service';
import Career from '../../Models/Users/Careers/Career';
import Technical from '../../Models/Users/Careers/Technical';
import Course from '../../Models/Users/Careers/Course';
import Commission from '../../Models/Commission/commission';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import Subjects from '../../Models/Subjects/Subjects';
import { RegistrationStudentOrTeacher } from '../../Models/Users/Student';
import { NotificationService } from '../../Services/notification/notification.service';
import { AuthService } from '../../Services/Auth/auth-service';

@Component({
  selector: 'app-inscription-to-commission',
  imports: [ReactiveFormsModule],
  templateUrl: './inscription-to-commission.html',
  styleUrl: './inscription-to-commission.css'
})
export class InscriptionToCommission implements OnInit, OnDestroy {

  listCommission: Commission[] = []
  form!: FormGroup
  legajo!: FormControl
  com!: Commission
  sub!: Subjects

  constructor(public careerService: CareerService,
    private notificationService: NotificationService,
    public auth: AuthService,
    public commisionService: CommissionService) {
    this.legajo = new FormControl("", Validators.required)
    this.form = new FormGroup({
      legajo: this.legajo
    })
  }

  ngOnInit(): void {
    this.getCommisionByNameProgram()
    console.log(this.auth.getRole())
  }

  ngOnDestroy(): void {
    this.careerService.setCareerSelected(null)
  }

  getCommisionByNameProgram() {

    const program: string = this.careerService.getCareerSelected() ?? "career";

    this.commisionService.getByProgram(program).subscribe({
      next: (res) => {
        this.listCommission = res
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  addStudentToCommision() {

    const request: RegistrationStudentOrTeacher = {
      legajo: this.legajo.value,
      subjectsId: this.sub.id
    }

    this.commisionService.registerStudentToCommissionByManager(this.com.id, request).subscribe({
      next: (res) => {
        this.notificationService.success("Se registro el alumno correctamente")
      },
      error: (err) => {
        this.notificationService.error("El alumno no existe o ya está anotado a esta materia.", true)
        console.log(err)
      }
    })
  }
  addTeacherToCommision() {

    const request: RegistrationStudentOrTeacher = {
      legajo: this.legajo.value,
      subjectsId: this.sub.id
    }

    this.commisionService.registerTeacherToCommissionByManager(this.com.id, request).subscribe({
      next: (res) => {
        this.notificationService.success("Se registro el profesor correctamente")
      },
      error: (err) => {
        this.notificationService.error("Hubo un error", true)
        console.log(err)
      }
    })
  }

  registerStudent(commissionId: number, subjectsId: number) {
    this.commisionService.regiterByStudent(commissionId, subjectsId).subscribe({
      next: (res) => {
        this.notificationService.success("Te registraste correctamente")
      },
      error: (err) => {
        this.notificationService.error("Hubo un error", true)
        console.log(err)
      }
    })
  }

  setCommissionAndSubject(com: Commission, s: Subjects) {
    this.com = com
    this.sub = s
  }
}
