import { Component, OnInit } from '@angular/core';
import Teacher from '../../../Models/Users/Teachers';
import { TeacherService } from '../../../Services/Users/teacher-service';
import { UserForm } from "../../../components/user-form/user-form";

@Component({
  selector: 'app-teachers',
  imports: [UserForm],
  templateUrl: './teachers.html',
  styleUrl: './teachers.css'
})
export class Teachers implements OnInit {
  teachers !: Teacher[];

  constructor(
    private service: TeacherService
  ) { }

  ngOnInit(): void {
    this.getTeachers();
  }

  getTeachers() {
    this.service.getTeachers().subscribe({
      next: (data) => { this.teachers = data },
      error: (error) => { console.error(error) }
    })
  }
  deleteTeacher(id: number) {
    this.service.deleteTeacher(id).subscribe({
      next: (data) => { this.getTeachers() },
      error: (error) => { console.error(error) }
    })
  }
}
