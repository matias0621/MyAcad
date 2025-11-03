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
  added = new EventEmitter<any[]>
  formPrograms !: FormGroup

  constructor(
    private service: CareerService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.formPrograms = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(60), Validators.pattern(/^[a-zA-Z\s]+$/)]],
      description: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(200)]],
      durationMonths: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(100), Validators.pattern(/^[0-9]+$/)]],
      monthlyFee: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(30), Validators.pattern(/^[0-9]+$/)]],
      annualFee: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(30), Validators.pattern(/^[0-9]+$/)]],
      active: [true, [Validators.required]]
    })
  }

  OnSubmit() {
    this.service.postCareer(this.formPrograms.value, this.endpoint).subscribe({
      next: (data) => {
        this.formPrograms.reset({ active: true });
        this.service.getCareers(this.endpoint).subscribe({
          next: (programs) => {
            alert('Programa creado exitosamente.');
            this.added.emit(programs);
            // Cerrar el modal
            const modalElement = document.getElementById('modal-add');
            if (modalElement) {
              const modal = (window as any).bootstrap.Modal.getInstance(modalElement);
              if (modal) {
                modal.hide();
              }
            }
          },
          error: (error) => {
            console.error('Error al obtener programas:', error);
          }
        });
      },
      error: (error) => { 
        console.error('Error al crear programa:', error);
      }
    })
  }

  cleanForm(){
    this.formPrograms.reset();
  }
}
