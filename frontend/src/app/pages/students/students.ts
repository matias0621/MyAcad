import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { StudentService } from '../../Services/student-service';
import Student from '../../Models/Student';

@Component({
  selector: 'app-students',
  imports: [ReactiveFormsModule],
  templateUrl: './students.html',
  styleUrl: './students.css'
})
export class Students implements OnInit {
  students !: Student[];
  form !: FormGroup;

  constructor(
    private service: StudentService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      name: [''],
      lastName: [''],
      email: [''],
      username: [''],
      password: [''],
      legajo: ['']
    })

    this.getStudents();
  }

  OnSubmit() {
    this.service.postStudent(this.form.value).subscribe({
      next: (data) => {this.getStudents() },
      error: (error) => { console.error(error) }
    })
  }

  getStudents() {
    this.service.getStudents().subscribe({
      next: (data) => { this.students = data },
      error: (error) => { console.error(error) }
    })
  }
}
