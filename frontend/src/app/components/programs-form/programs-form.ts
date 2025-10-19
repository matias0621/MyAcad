import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CareerService } from '../../Services/CareerService/career-service';

@Component({
  selector: 'app-programs-form',
  imports: [ReactiveFormsModule],
  templateUrl: './programs-form.html',
  styleUrl: './programs-form.css'
})
export class ProgramsForm implements OnInit {
  @Input()
  endpoint: string = "";

  @Output()
  added = new EventEmitter<void>();
  formPrograms !: FormGroup

  constructor(
    private service: CareerService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.formPrograms = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(60), Validators.pattern(/^[a-zA-Z\s]+$/)]],
      description: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(200), Validators.pattern(/^[a-zA-Z\s]+$/)]],
      durationMonths: ['', [Validators.required, Validators.min(1), Validators.max(30), Validators.pattern(/^[0-9]+$/)]],
      monthlyFee: ['', [Validators.required, Validators.min(4), Validators.max(30), Validators.pattern(/^[0-9]+$/)]],
      annualFee: ['', [Validators.required, Validators.min(4), Validators.max(30), Validators.pattern(/^[0-9]+$/)]],
      active: [true, [Validators.required]]
    })
  }

  OnSubmit() {
    const raw = this.formPrograms.value;
    const payload = {
      ...raw,
      durationMonths: Number(raw.durationMonths),
      monthlyFee: Number(raw.monthlyFee),
      annualFee: Number(raw.annualFee)
    };

    this.service.postCareer(payload).subscribe({
      next: (data) => {
        console.log('P creada exitosamente:', data);
        this.formPrograms.reset();
        this.added.emit()
      },
      error: (error) => { console.error(error) }
    })
  }

  cleanForm(){
    this.formPrograms.reset();
  }
}
