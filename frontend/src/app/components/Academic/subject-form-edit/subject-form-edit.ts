import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { SubjectsService } from '../../../Services/Subjects/subjects-service';
import { ActivatedRoute, Router } from '@angular/router';
import Subjects from '../../../Models/Subjects/Subjects';

@Component({
  selector: 'app-subject-form-edit',
  imports: [ReactiveFormsModule],
  templateUrl: './subject-form-edit.html',
  styleUrl: './subject-form-edit.css'
})
export class SubjectFormEdit implements OnInit {

  form!:FormGroup
  name!:FormControl
  description!:FormControl
  semesters!:FormControl
  subjectActive!:FormControl
  academicStatus!:FormControl
  id!:string

  constructor(public subjectService:SubjectsService, private activatedRouter:ActivatedRoute, private router:Router){
    this.name = new FormControl("", [Validators.required, Validators.maxLength(30)])
    this.description = new FormControl("", [Validators.required, Validators.maxLength(300)])
    this.semesters = new FormControl("", [Validators.required, Validators.min(1), Validators.maxLength(50)])
    this.subjectActive = new FormControl("")
    this.academicStatus = new FormControl("", [Validators.required])
    this.id = this.activatedRouter.snapshot.params['id']

    this.form = new FormGroup({
      name: this.name,
      description: this.description,
      semesters: this.semesters,
      subjectActive: this.subjectActive,
      academicStatus: this.academicStatus
    })
  }

  ngOnInit(): void {
    this.subjectService.getSubjectById(this.id).subscribe({
      next: (res) => {
        this.name.setValue(res.name)
        this.description.setValue(res.description)
        this.semesters.setValue(res.semesters)
        this.subjectActive.setValue(res.subjectActive)
      },
      error: (err) => {
        console.log(err)
      }
    })
  }



  OnSubmit(){

    if (this.form.invalid){
      alert("Complete todos los campos de la materia para subirla")
      return
    }

    
    const subjectUpdate:Subjects = {
      id: parseInt(this.id),
      name: this.name.value,
      description: this.description.value,
      semesters: this.semesters.value,
      academicStatus: this.academicStatus.value,
      subjectActive: this.subjectActive.value
    }
    this.subjectService.putSubject(subjectUpdate).subscribe({
      next: (res) => {
        console.log
        alert("Se subio correctamente la nueva materia")
        this.router.navigate([""])
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
