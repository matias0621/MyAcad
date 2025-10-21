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
    this.service.deleteCareer(id).subscribe({
      next: (data) => { this.getEngineerings() },
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


