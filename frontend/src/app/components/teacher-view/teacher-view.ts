import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ProgramService } from '../../Services/program-service';
import { AuthService } from '../../Services/Auth/auth-service';
import Program from '../../Models/Program/Program';

@Component({
  selector: 'app-teacher-view',
  imports: [RouterLink],
  templateUrl: './teacher-view.html',
  styleUrl: './teacher-view.css'
})
export class TeacherView implements OnInit {

  listOfProgram: Program[] = []
  teacherName!: string
  loading: boolean = true

  constructor(
    public programService: ProgramService, 
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.getProgramByTeacherId()
  }

  getProgramByTeacherId() {
    const token: any = this.authService.getDecodedToken()
    if (!token) {
      this.loading = false
      return
    }
    
    this.teacherName = token.name

    this.programService.getProgramsByTeacher(token.id).subscribe({
      next: (res) => {
        this.listOfProgram = res
        this.loading = false
      },
      error: (error) => {
        console.error('Error fetching programs:', error)
        this.loading = false
      }
    })
  }
}
