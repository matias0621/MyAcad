import { Component, OnInit } from '@angular/core';
import { CareerService } from '../../../Services/CareerService/career-service';
import Career from '../../../Models/Users/Careers/Career';
import { Router } from '@angular/router';
import Technical from '../../../Models/Users/Careers/Technical';
import { ProgramsForm } from '../../../components/programs-form/programs-form';
import { ReactiveFormsModule } from '@angular/forms';
import { ProgramsEditForm } from "../../../components/programs-edit-form/programs-edit-form";
import { ProgramsList } from "../../../components/programs-list/programs-list";

@Component({
  selector: 'app-technicals',
  imports: [ProgramsForm, ReactiveFormsModule, ProgramsEditForm, ProgramsList],
  templateUrl: './technicals.html',
  styleUrl: './technicals.css'
})
export class Technicals implements OnInit {
  technicals !: Technical[];
  showDisabled = false;

  constructor(
    private service: CareerService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.getTechnicals();
  }

  getTechnicals() {
    this.service.getCareers('technicals').subscribe({
      next: (data) => {
        this.technicals = data;
      },
      error: (error) => {
        console.error(error);
      }
    })
  }

  deleteTechnical(id: number) {
    this.service.deleteCareer(id, 'technicals').subscribe({
      next: (data) => { alert(`Tecnicatura con ID ${id} eliminada exitosamente.`); this.getTechnicals() },
      error: (error) => { console.error(error) }
    })
  }


  viewDisabled(technical: Technical) {
    if (confirm(`¿Deseas activar "${technical.name}"?`)) {
      const updatedTechnical = { ...technical, active: true };
      this.service.updateByEndpoint(updatedTechnical, 'technicals').subscribe({
        next: (response) => {
          alert(`Tecnicatura "${technical.name}" activada exitosamente.`);
          this.getTechnicals();
        },
        error: (error) => {
          alert('Error al activar la tecnicatura. Por favor, intenta nuevamente.');
        }
      });
    }
  }

  toggleDisabledView() {
    this.showDisabled = !this.showDisabled;
  }
}
