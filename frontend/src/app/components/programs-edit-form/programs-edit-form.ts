import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CareerService } from '../../Services/CareerService/career-service';
import Career from '../../Models/Users/Careers/Career';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-programs-edit-form',
  imports: [ReactiveFormsModule],
  templateUrl: './programs-edit-form.html',
  styleUrl: './programs-edit-form.css'
})
export class ProgramsEditForm implements OnInit {
  formPrograms!: FormGroup;
  careerId!: string;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private cService: CareerService
  ) { }

  ngOnInit(): void {
    this.careerId = this.route.snapshot.params['id'];

    this.formPrograms = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(60)]],
      description: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(200)]],
      durationMonths: ['', [Validators.required, Validators.pattern(/^[0-9]+$/)]],
      monthlyFee: ['', [Validators.required, Validators.pattern(/^[0-9]+$/)]],
      annualFee: ['', [Validators.required, Validators.pattern(/^[0-9]+$/)]],
      active: [true, [Validators.required]]
    });

  }

  onSubmit() {
    const updateCareer: Career = this.formPrograms.value;
    this.cService.updateCareer(updateCareer).subscribe(() => {
      alert('Carrera Actualizada :D');
    })
  }

  send() {
    if (this.formPrograms.invalid) {
      this.formPrograms.markAllAsTouched();
      return;
    }

    const careerData: Career = {
      id: Number(this.careerId),
      name: this.formPrograms.value.name,
      description: this.formPrograms.value.description,
      durationMonths: Number(this.formPrograms.value.durationMonths),
      monthlyFee: Number(this.formPrograms.value.monthlyFee),
      annualFee: Number(this.formPrograms.value.annualFee),
      active: this.formPrograms.value.active
    };

    this.cService.updateCareer(careerData).subscribe({
      next: (data) => {
        console.log('Carrera actualizada:', data);
        alert('Carrera actualizada correctamente');
      },
      error: (err) => {
        console.error('Error al actualizar la carrera.', err);
        alert('Error al actualizar la carrera.');
      },
    });
  }

  cleanForm(){
    this.formPrograms.reset();
  }
}