import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../../Services/Users/user-service';

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
  added = new EventEmitter<any[]>;

  userId !: number
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
      email: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      password: ['', [Validators.minLength(6), Validators.maxLength(15)]]
    })
  }


  OnSubmit() {
    const modifiedUser = { id: this.userId, ...this.form.value }
    
    this.service.putUser(modifiedUser, this.endpoint).subscribe({
      next: (data) => {
        console.log('Usuario modificado exitosamente:');
        this.form.reset();
        this.service.getUsers(this.endpoint).subscribe({
          next: (data) => { 
            alert('Usuario editado exitosamente.');
            this.added.emit(data);
            // Cerrar el modal
            const modalElement = document.getElementById('modal-edit');
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

  loadData(userData: any) {
    this.userId = userData.id

    const mappedData = {
      name: userData.name,
      lastName: userData.lastName,
      email: userData.email
    }

    this.form.patchValue(mappedData)
  }

  cleanForm() {
    this.form.reset();
  }
}
