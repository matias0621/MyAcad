import { Component, OnInit } from '@angular/core';
import { CareerService } from '../../../Services/CareerService/career-service';
import Career from '../../../Models/Users/Careers/Career';
import { ProgramsForm } from "../../../components/programs-form/programs-form";

@Component({
  selector: 'app-engineerings',
  imports: [ProgramsForm],
  templateUrl: './engineerings.html',
  styleUrl: './engineerings.css'
})
export class Engineerings implements OnInit {
  engineerings !: Career[];

  constructor(
    private service: CareerService
  ) { }

  ngOnInit(): void {
    this.getEngineerings();
  }

  getEngineerings() {
    this.service.getCareers().subscribe({
      next: (data) => { this.engineerings = data },
      error: (error) => { console.error(error) }
    })
  }

  deleteEngineering(id: string) {
    this.service.deleteCareer(id).subscribe({
      next: (data) => { this.getEngineerings() },
      error: (error) => { console.error(error) }
    })
  }

}
