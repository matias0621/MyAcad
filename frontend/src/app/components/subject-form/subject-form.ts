import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';

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
  semester!:FormControl

  constructor(){
    this.name = new FormControl("")
    this.description = new FormControl("")
    this.semester = new FormControl("")

    this.form = new FormGroup({
      name: this.name,
      description: this.description,
      semester: this.semester
    })
  }

  OnSubmit(){
    
  }

  cleanForm(){
    this.form.reset();
  }
}
