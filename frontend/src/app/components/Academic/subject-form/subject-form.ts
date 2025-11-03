import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { SubjectsService } from '../../../Services/Subjects/subjects-service';
import Subjects from '../../../Models/Subjects/Subjects';

@Component({
  selector: 'app-subject-form',
  imports: [ReactiveFormsModule],
  templateUrl: './subject-form.html',
  styleUrl: './subject-form.css'
})
export class SubjectForm {

  form!:FormGroup
  name!:FormControl
  description!:FormControl
  semesters!:FormControl
  academicStatus!:FormControl


  @Output()
  added = new EventEmitter<void>;

  

  constructor(public subjectService:SubjectsService){
    this.name = new FormControl("", [Validators.required, Validators.maxLength(30)])
    this.description = new FormControl("", [Validators.required, Validators.maxLength(300)])
    this.semesters = new FormControl("", [Validators.required, Validators.maxLength(50)])
    this.academicStatus = new FormControl("", [Validators.required])

    this.form = new FormGroup({
      name: this.name,
      description: this.description,
      semesters: this.semesters,
      subjectActive: new FormControl(true),
      academicStatus: this.academicStatus
    })
  }



  OnSubmit(){

    if (this.form.invalid){
      alert("Complete todos los campos de la materia para subirla")
      return
    }

    
    console.log(this.form.value)

    this.subjectService.postSubject(this.form.value).subscribe({
      next: (res) => {
        console.log
        alert("Se subio correctamente la nueva materia")
        this.added.emit()
        console.log(res)
      },
      error: (err) => {
        alert("Algo salio mal")
        console.log(err)
      }
    })
  }

  cleanForm(){
    this.form.reset();
  }



}
