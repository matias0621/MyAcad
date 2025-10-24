import { Component, EventEmitter, input, Input, OnInit, Output } from '@angular/core';
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
  added = new EventEmitter<void>;
  form !: FormGroup;

  constructor(
    private service: UserService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30), Validators.pattern(/^[a-zA-Z]+$/)]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(30), Validators.pattern(/^[a-zA-Z]+$/)]],
      email: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(15)]]
    })
  }

  OnSubmit() {
    this.service.postUser(this.form.value, this.endpoint).subscribe({
      next: (data) => {
        console.log('Usuario creado exitosamente:', data);
        this.form.reset();
        this.added.emit()
      },
      error: (error) => { console.error(error) }
    })
  }
  
  cleanForm(){
    this.form.reset();
  }
}
