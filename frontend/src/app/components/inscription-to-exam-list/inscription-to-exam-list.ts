import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { InscriptionToFinalExamService } from '../../Services/InscriptionToFinalExam/inscription-to-final-exam-service';
import { InscriptionToFinalExam } from '../../Models/InscriptionToFinalExam/InscriptionToFinalExam';
import { InscriptionToExamForm } from "../inscription-to-exam-form/inscription-to-exam-form";
import { InscriptionToExamFormEdit } from "../inscription-to-exam-form-edit/inscription-to-exam-form-edit";
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-inscription-to-exam-list',
  imports: [InscriptionToExamForm, InscriptionToExamFormEdit, DatePipe],
  templateUrl: './inscription-to-exam-list.html',
  styleUrl: './inscription-to-exam-list.css'
})
export class InscriptionToExamList implements OnInit {
  inscriptionList !: InscriptionToFinalExam[];
  selectedInscription ?: InscriptionToFinalExam;

  constructor(public inscriptionService:InscriptionToFinalExamService, private crd:ChangeDetectorRef){}

  ngOnInit(): void {
    this.getAllInscription()
  }
  
  getAllInscription(){
    this.inscriptionService.getAllInscription().subscribe({
      next: (res) => {
        this.inscriptionList = res;
        this.crd.detectChanges()
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  addStudent(idInscription:number){
    this.inscriptionService.addStudentToFinalExam(idInscription).subscribe({
      next: (res) => {
        alert("Te anotaste correctamente")
      },
      error: (err) => {
        alert("Hubo un error, intente mas tarde")
      }
    })
  }

}
