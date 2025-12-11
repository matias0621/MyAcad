import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../Services/Auth/auth-service';
import { ProgramService } from '../../Services/Program/program-service';
import Program from '../../Models/Program/Program';
import { RouterLink } from '@angular/router';
import { ViewStudent } from '../../components/StudentView/view-student/view-student';
import { TeacherView } from '../../components/Teacher/teacher-view/teacher-view';

@Component({
  selector: 'app-home',
  imports: [RouterLink, ViewStudent, TeacherView],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {
  programs?: Program[]
  token  !: any;

  constructor(
    public authService: AuthService,
    private service: ProgramService
  ) { }

  ngOnInit(): void {
    this.token = this.authService.getToken();

    if (this.authService.getRole() === 'STUDENT') {
      this.getProgramsByStudent(this.token.id)
    } else if (this.authService.getRole() === 'TEACHER') {
      this.getProgramsByTeacher(this.token.id)
    }
  }

  getProgramsByStudent(studentId: number) {
    this.service.getProgramsByStudent(studentId).subscribe({
      next: (data) => {
        this.programs = data;
      },
      error: (error) => {
        console.error(error);
      }
    });
  }

  getProgramsByTeacher(teacherId: number) {
    this.service.getProgramsByTeacher(teacherId).subscribe({
      next: (data) => {
        this.programs = data;
      },
      error: (error) => {
        console.error(error);
      }
    });
  }
}
