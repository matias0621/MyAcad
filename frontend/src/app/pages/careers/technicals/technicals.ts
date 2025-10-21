import { Component, OnInit } from '@angular/core';
import { CareerService } from '../../../Services/CareerService/career-service';
import Career from '../../../Models/Users/Careers/Career';
import { Router } from '@angular/router';
import Technical from '../../../Models/Users/Careers/Technical';
import { ProgramsForm } from '../../../components/programs-form/programs-form';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-technicals',
  imports: [ProgramsForm, ReactiveFormsModule],
  templateUrl: './technicals.html',
  styleUrl: './technicals.css'
})
export class Technicals implements OnInit{
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
    this.service.getCareers('careers').subscribe({
      next: (data) => { 
        this.technicals = data;
      },
      error: (error) => { 
        console.error('E', error);
      }
    })
  }

  deleteTechnical(id: number) {
    this.service.deleteCareer(id).subscribe({
      next: (data) => { this.getTechnicals() },
      error: (error) => { console.error(error) }
    })
  }


  viewDisabled(career: Career) {
    if (confirm(`¿Deseas activar "${career.name}"?`)) {
        career.active = true;
        this.router.navigate(['/programs-edit-form', career.id]);
    }
  }

  toggleDisabledView() {
    this.showDisabled = !this.showDisabled;
  }
}
