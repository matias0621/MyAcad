import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../../Services/Users/user-service';
import { NotificationService } from '../../../Services/notification/notification.service';


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
    private fb: FormBuilder,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30), Validators.pattern(/^[A-Za-zÁÉÍÓÚáéíóúñÑ]+(?: [A-Za-zÁÉÍÓÚáéíóúñÑ]+)*$/)]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(30), Validators.pattern(/^[A-Za-zÁÉÍÓÚáéíóúñÑ]+(?: [A-Za-zÁÉÍÓÚáéíóúñÑ]+)*$/)]],
      dni: ['', [Validators.required, Validators.min(1000000), Validators.max(999999999), Validators.pattern(/^[0-9]+$/)]],
      email: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]]
    })
  }

  OnSubmit() {
    const user = {...this.form.value, active: true};
    this.service.postUser(user, this.endpoint).subscribe({
      next: (data) => {
        this.notificationService.success('Usuario creado exitosamente');
        this.form.reset();
        this.service.getUsers(this.endpoint).subscribe({
          next: (data) => { this.added.emit(data) },
          error: (error) => { console.error(error) }
        })
      },
      error: (error) => { 
        this.notificationService.error('Error al crear el usuario', true);
        console.error(error);
      }
    })
  }

  cleanForm() {
    this.form.reset();
  }

}
