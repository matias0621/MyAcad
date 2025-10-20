import { Component, OnInit } from '@angular/core';
import { CareerService } from '../../../Services/CareerService/career-service';
import Career from '../../../Models/Users/Careers/Career';
import { ProgramsForm } from "../../../components/programs-form/programs-form";
import { Router } from '@angular/router';

@Component({
  selector: 'app-engineerings',
  imports: [ProgramsForm],
  templateUrl: './engineerings.html',
  styleUrl: './engineerings.css'
})
export class Engineerings implements OnInit {
  engineerings !: Career[];
  showDisabled = false; 

  constructor(
    private service: CareerService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.getEngineerings();
  }

  getEngineerings() {
    this.service.getCareers().subscribe({
      next: (data) => {
        this.engineerings = data.filter(c => c.careerType === 'ENGINEERING');
        console.log('Ingenierías filtradas:', this.engineerings);
      },
      error: (error) => { console.error(error) }
    })
  }

  deleteEngineering(id: string) {
    this.service.deleteCareer(id).subscribe({
      next: (data) => { this.getEngineerings() },
      error: (error) => { console.error(error) }
    })
  }

  updateCareer(career: Career) {
    this.router.navigate(['/programs-edit-form', career.id]);
  }

  postCareer(career: Career) {
    this.service.postCareer(career).subscribe({
      next: (data) => { this.getEngineerings() },
      error: (error) => { console.error(error) }
    })
  }

  viewDisabled(career: Career) {
      if (confirm(`¿Deseas activar "${career.name}"?`)) {
        career.active = true;
        this.updateCareer(career);
      }
    }

  toggleDisabledView() {
    this.showDisabled = !this.showDisabled;
  }

}
