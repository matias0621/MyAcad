import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { InscriptionToFinalExamService } from '../../Services/InscriptionToFinalExam/inscription-to-final-exam-service';

@Component({
  selector: 'app-inscription-to-exam-list',
  imports: [],
  templateUrl: './inscription-to-exam-list.html',
  styleUrl: './inscription-to-exam-list.css'
})
export class InscriptionToExamList implements OnInit {

  constructor(public inscriptionService:InscriptionToFinalExamService, private crd:ChangeDetectorRef){}

  ngOnInit(): void {
    this.getAllInscription()
  }
  
  getAllInscription(){
    this.inscriptionService.getAllInscription().subscribe({
      next: (res) => {
        this.inscriptionService.inscriptionList = [...res]
        this.crd.detectChanges()
        console.log(this.inscriptionService.inscriptionList)
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  addStudent(idInscription:number){
    this.inscriptionService.addStudentToFinalExam(idInscription).subscribe({
      next: (res) => {
        alert("Te anotaste corrextamente")
      },
      error: (err) => {
        alert("Hubo un error intente mas tarde")
      }
    })
  }

}
