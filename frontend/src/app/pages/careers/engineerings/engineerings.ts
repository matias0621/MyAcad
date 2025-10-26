import { Component, OnInit } from '@angular/core';
import { CareerService } from '../../../Services/CareerService/career-service';
import Career from '../../../Models/Users/Careers/Career';
import { Router } from '@angular/router';
import { ProgramsForm } from '../../../components/programs-form/programs-form';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-engineerings',
  imports: [ProgramsForm, ReactiveFormsModule],
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
    this.service.getCareers('careers').subscribe({
      next: (data) => { this.engineerings = data },
      error: (error) => { console.error(error) }
    })
  }

  deleteEngineering(id: number) {
    this.service.deleteCareer(id, 'careers').subscribe({
      next: (data) => { alert(`Carrera con ID ${id} eliminada exitosamente.`);
        this.getEngineerings() },
      error: (error) => { console.error(error) }
    })
  }
  

  viewDisabled(career: Career) {
    if (confirm(`¿Deseas activar "${career.name}"?`)) {
      const updatedCareer = { ...career, active: true };
      this.service.updateCareer(updatedCareer).subscribe({
        next: (response) => {
          alert(`Carrera "${career.name}" activada exitosamente.`);
          this.getEngineerings();
        },
        error: (error) => {
          alert('Error al activar la carrera. Por favor, intenta nuevamente.');
        }
      });
    }
  }

  toggleDisabledView() {
    this.showDisabled = !this.showDisabled;
  }

}


