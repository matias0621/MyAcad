import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { SubjectForm } from '../../components/subject-form/subject-form';
import { SubjectsService } from '../../Services/Subjects/subjects-service';

@Component({
  selector: 'app-subjects',
  imports: [ReactiveFormsModule, SubjectForm],
  templateUrl: './subjects.html',
  styleUrl: './subjects.css'
})
export class Subjects implements OnInit {

  constructor(public subjectService:SubjectsService){}


  ngOnInit(): void {
    this.getAllSubject()
  }

  
  getAllSubject(){
    this.subjectService.getAllSubject().subscribe({
      next: (res) => {
        this.subjectService.listSubject = res
      },
      error: (err) => {
        console.log(err)
      }
    })
  }


    deleteSubject(id:number){
    this.subjectService.deleteSubject(id.toString()).subscribe({
      next: (res) => {
        alert("Se elimino correctamente")
        this.getAllSubject()
      },
      error: (err) => {
        alert("No se pudo eliminar la materia")
      }
    })
  }
}
