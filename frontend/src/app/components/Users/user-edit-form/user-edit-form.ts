import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../../Services/Users/user-service';
import { NotificationService } from '../../../Services/notification/notification.service';

@Component({
  selector: 'app-user-edit-form',
  imports: [ReactiveFormsModule],
  templateUrl: './user-edit-form.html',
  styleUrl: './user-edit-form.css'
})
export class UserEditForm implements OnInit {
  @Input()
  endpoint: string = "";

  @Output()
  added = new EventEmitter<any>;

  userId !: number
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
      email: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      password: ['', [Validators.minLength(6), Validators.maxLength(15)]]
    })
  }


  OnSubmit() {
    const modifiedUser = { id: this.userId, ...this.form.value }
    
    this.service.putUser(modifiedUser, this.endpoint).subscribe({
      next: (data) => {
        this.notificationService.success('Usuario modificado exitosamente');
        this.form.reset();
        this.service.getUsers(this.endpoint).subscribe({
          next: (data) => { this.added.emit(true) },
          error: (error) => { console.error(error) }
        })
      },
      error: (error) => { 
        this.notificationService.error('Error al modificar el usuario', true);
        console.error(error);
      }
    })
  }

  loadData(userData: any) {
    this.userId = userData.id

    const mappedData = {
      name: userData.name,
      lastName: userData.lastName,
      email: userData.email,
      //Cargamos dni en caso de que tenga que ser modificado por estar mal cargado.
      dni: userData.dni
    }

    this.form.patchValue(mappedData)
  }

  cleanForm() {
    this.form.reset();
  }
}
