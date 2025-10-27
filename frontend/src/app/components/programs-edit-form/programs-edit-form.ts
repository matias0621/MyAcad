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
  @Input()
  endpoint: string = "";

  @Output()
  added = new EventEmitter<any[]>;

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

  OnSubmit() {
    const modifiedCareer = { id: this.careerId, ...this.formPrograms.value };

    this.cService.updateCareer(modifiedCareer).subscribe({
      next: (data) => {
        console.log('Programa modificado exitosamente:');
        this.formPrograms.reset();
        this.cService.getCareers(this.endpoint).subscribe({
          next: (data) => { this.added.emit(data) },
          error: (error) => { console.error(error) }
        })
      },
      error: (error) => { console.error(error) }
    })
  }

  loadData(careerData: any) {
    this.careerId = careerData.id

    const mappedData = {
      name: careerData.name,
      description: careerData.description,
      durationMonths: careerData.durationMonths,
      monthlyFee: careerData.monthlyFee,
      annualFee: careerData.annualFee,
      active: careerData.active
    }

    this.formPrograms.patchValue(mappedData)
  }

  cleanForm() {
    this.formPrograms.reset();
  }
}