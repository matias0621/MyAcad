import { Component, OnInit } from '@angular/core';
import { InscriptionToFinalExamService } from '../../Services/InscriptionToFinalExam/inscription-to-final-exam-service';

@Component({
  selector: 'app-inscription-to-exam-list',
  imports: [],
  templateUrl: './inscription-to-exam-list.html',
  styleUrl: './inscription-to-exam-list.css'
})
export class InscriptionToExamList implements OnInit {

  constructor(public inscriptionService:InscriptionToFinalExamService){}

  ngOnInit(): void {
    this.inscriptionService.getAllInscription().subscribe({
      next: (res) => {
        this.inscriptionService.inscriptionList = [...res]
      },
      error: (err) => {
        console.log(err)
      }
    })
  }
  

}
