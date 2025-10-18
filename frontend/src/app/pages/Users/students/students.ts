import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import Student from '../../../Models/Users/Student';
import { StudentService } from '../../../Services/Users/student-service';
import { UserForm } from "../../../components/user-form/user-form";

@Component({
  selector: 'app-students',
  imports: [ReactiveFormsModule, UserForm],
  templateUrl: './students.html',
  styleUrl: './students.css'
})
export class Students implements OnInit {
  students !: Student[];

  constructor(
    private service: StudentService
  ) { }

  ngOnInit(): void {
    this.getStudents();
  }

  getStudents() {
    this.service.getStudents().subscribe({
      next: (data) => { this.students = data },
      error: (error) => { console.error(error) }
    })
  }

  deleteStudent(id: number) {
    this.service.deleteStudent(id).subscribe({
      next: (data) => { this.getStudents() },
      error: (error) => { console.error(error) }
    })
  }
}
