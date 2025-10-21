import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { SubjectsService } from '../../Services/Subjects/subjects-service';

@Component({
  selector: 'app-subject-form',
  imports: [ReactiveFormsModule],
  templateUrl: './subject-form.html',
  styleUrl: './subject-form.css'
})
export class SubjectForm implements OnInit {

  form!:FormGroup
  name!:FormControl
  description!:FormControl
  semesters!:FormControl

  

  constructor(public subjectService:SubjectsService){
    this.name = new FormControl("", [Validators.required, Validators.maxLength(30)])
    this.description = new FormControl("", [Validators.required, Validators.maxLength(300)])
    this.semesters = new FormControl("", [Validators.required, Validators.maxLength(50)])

    this.form = new FormGroup({
      name: this.name,
      description: this.description,
      semesters: this.semesters
    })
  }

  ngOnInit(): void {
    this.getAllSubject()
  }

  OnSubmit(){

    if (this.form.invalid){
      alert("Complete todos los campos de la materia para subirla")
      return
    }

    this.subjectService.postSubject(this.form.value).subscribe({
      next: (res) => {
        alert("Se subio correctamente la nueva materia")
        console.log(res)
        this.getAllSubject()
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
}
