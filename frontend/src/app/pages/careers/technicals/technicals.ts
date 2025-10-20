import { Component, OnInit } from '@angular/core';
import { CareerService } from '../../../Services/CareerService/career-service';
import Career from '../../../Models/Users/Careers/Career';
import { ProgramsForm } from "../../../components/programs-form/programs-form";
import { Router } from '@angular/router';

@Component({
  selector: 'app-technicals',
  imports: [ProgramsForm],
  templateUrl: './technicals.html',
  styleUrl: './technicals.css'
})
export class Technicals implements OnInit{
  technicals !: Career[];
  showDisabled = false;

  constructor(
    private service: CareerService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.getTechnicals();
  }

  getTechnicals() {
    this.service.getCareers().subscribe({
      next: (data) => { 
        this.technicals = data.filter(c => c.careerType === 'TECHNICAL');
        console.log('Tecnicaturas filtradas:', this.technicals);
      },
      error: (error) => { 
        console.error('Error al cargar tecnicaturas:', error);
      }
    })
  }

  deleteTechnical(id: string) {
    this.service.deleteCareer(id).subscribe({
      next: (data) => { this.getTechnicals() },
      error: (error) => { console.error(error) }
    })
  }

  updateCareer(career: Career) {
    this.router.navigate(['/programs-edit-form', career.id]);
  }

  postCareer(career: Career) {
    this.service.postCareer(career).subscribe({
      next: (data) => { this.getTechnicals() },
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
