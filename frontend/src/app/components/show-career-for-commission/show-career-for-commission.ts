import { Component, OnInit } from '@angular/core';
import Program from '../../Models/Program/Program';
import { ProgramService } from '../../Services/Program/program-service';
import { Router } from '@angular/router';
import { NotificationService } from '../../Services/notification/notification.service';

@Component({
  selector: 'app-show-career-for-commission',
  imports: [],
  templateUrl: './show-career-for-commission.html',
  styleUrl: './show-career-for-commission.css',
})
export class ShowCareerForCommission implements OnInit {
  listProgram!: Program[];
  
  constructor(private program: ProgramService, private router: Router) {}

  ngOnInit(): void {
    this.program.getPrograms().subscribe({
      next: (res) => {
        this.listProgram = res;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  setName(name: string) {
    this.changePage(name)
  }

  changePage(program: string) {
    this.router.navigate(['/register-student-to-commission/', program]);
  }
}
