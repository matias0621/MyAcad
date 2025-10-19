import { Component, OnInit } from '@angular/core';
import { CareerService } from '../../../Services/CareerService/career-service';
import Career from '../../../Models/Users/Careers/Career';
import { ProgramsForm } from "../../../components/programs-form/programs-form";

@Component({
  selector: 'app-technicals',
  imports: [ProgramsForm],
  templateUrl: './technicals.html',
  styleUrl: './technicals.css'
})
export class Technicals implements OnInit{
  technicals !: Career[];

  constructor(
    private service: CareerService
  ) { }

  ngOnInit(): void {
    this.getTechnicals();
  }

  getTechnicals() {
    this.service.getCareers().subscribe({
      next: (data) => { this.technicals = data },
      error: (error) => { console.error(error) }
    })
  }

  deleteTechnical(id: string) {
    this.service.deleteCareer(id).subscribe({
      next: (data) => { this.getTechnicals() },
      error: (error) => { console.error(error) }
    })
  }

}
