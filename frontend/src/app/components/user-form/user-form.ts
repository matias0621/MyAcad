import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../Services/Users/user-service';

@Component({
  selector: 'app-user-form',
  imports: [ReactiveFormsModule],
  templateUrl: './user-form.html',
  styleUrl: './user-form.css'
})
export class UserForm implements OnInit {
  @Input()
  endpoint: string = "";

  @Output()
  added = new EventEmitter<any[]>;

  form !: FormGroup;

  constructor(
    private service: UserService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30), Validators.pattern(/^[a-zA-Z]+$/)]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(30), Validators.pattern(/^[a-zA-Z]+$/)]],
      dni: ['', [Validators.required, Validators.min(1000000), Validators.max(999999999), Validators.pattern(/^[0-9]+$/)]],
      email: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]]
    })
  }

  OnSubmit() {
    this.service.postUser(this.form.value, this.endpoint).subscribe({
      next: (data) => {
        console.log('Usuario creado exitosamente');
        this.form.reset();
        this.service.getUsers(this.endpoint).subscribe({
          next: (data) => { 
            alert('Usuario creado exitosamente.');
            this.added.emit(data);
            // Cerrar el modal
            const modalElement = document.getElementById('modal-add');
            if (modalElement) {
              const modal = (window as any).bootstrap.Modal.getInstance(modalElement);
              if (modal) {
                modal.hide();
              }
            }
          },
          error: (error) => { console.error(error) }
        })
      },
      error: (error) => { console.error(error) }
    })
  }

  cleanForm() {
    this.form.reset();
  }

}
