import { NotificationService } from './../../../Services/notification/notification.service';
import { AuthService } from './../../../Services/Auth/auth-service';

import { Component, OnInit } from '@angular/core';
import { InscriptionToFinalExamService } from '../../../Services/InscriptionToFinalExam/inscription-to-final-exam-service';
import { InscriptionToFinalExam } from '../../../Models/InscriptionToFinalExam/InscriptionToFinalExam';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-register-to-final-exam',
  imports: [DatePipe],
  templateUrl: './register-to-final-exam.html',
  styleUrl: './register-to-final-exam.css'
})
export class RegisterToFinalExam implements OnInit {

  inscriptionList!:InscriptionToFinalExam[]

  constructor(
    public inscriptionFinalExamsService:InscriptionToFinalExamService,
    private authService:AuthService,
    private notificationService:NotificationService
  ){}

  ngOnInit(): void {
    const token:any = this.authService.getDecodedToken()


    console.log(this.authService.getToken())

    this.getAllInscriptionForStudent(token.id)
  }


  getAllInscriptionForStudent(id:number){
    this.inscriptionFinalExamsService.getAllInscriptionForStudentsRegister(id).subscribe({
      next: (res) => {
        this.inscriptionList = res
        console.log(res)
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  addStudent(inscriptionId:number){
    this.inscriptionFinalExamsService.addStudentToFinalExam(inscriptionId).subscribe({
      next: () => {
        this.notificationService.success("Te anotaste a al examen")
      },
      error: (err) => {
        this.notificationService.error("Hubo un error, intentelo denuevo mas tarde", true)
        console.log(err)
      }
    })
  }

}
