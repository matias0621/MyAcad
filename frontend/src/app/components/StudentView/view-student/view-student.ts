import { Component, OnInit } from '@angular/core';
import { ProgramService } from '../../../Services/Program/program-service';
import { AuthService } from '../../../Services/Auth/auth-service';
import Program from '../../../Models/Program/Program';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-view-student',
  imports: [RouterLink],
  templateUrl: './view-student.html',
  styleUrl: './view-student.css'
})
export class ViewStudent implements OnInit{

  listOfProgram:Program[] = []
  studentName!:string 

  constructor(public programService:ProgramService, public authService:AuthService){}

  ngOnInit(): void {
    this.getProgramByStudentId()
  }

  getProgramByStudentId(){

    const token:any = this.authService.getDecodedToken()
    if (!token) return;
    
    this.studentName = token.name

    console.log(this.authService.getRole())

    this.programService.getProgramsByStudent(token.id).subscribe({
      next: (res) => {
        this.listOfProgram = res
      },
      error: (error) => {
        console.error('Error fetching programs:', error);
      }
    })
    
  }
}
