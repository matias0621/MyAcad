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
  added = new EventEmitter<any>;

  form !: FormGroup;
  selectedFile!: File
  loadingCsv: boolean = false;

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
      email: ['', [Validators.email ,Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      csv: [null]
    })
  }

  OnSubmit() {
    if (this.form.invalid) {
      this.notificationService.warning('Formulario inválido. Por favor, complete todos los campos correctamente.');
      this.form.markAllAsTouched();
      return;
    }

    const user = {...this.form.value, active: true};
    this.service.postUser(user, this.endpoint).subscribe({
      next: (data) => {
        this.notificationService.success('Usuario creado exitosamente');
        this.form.reset();
        this.service.getUsers(this.endpoint).subscribe({
          next: (data) => { this.added.emit(true) },
          error: (error) => { console.error(error) }
        })
      },
      error: (error) => { 
        this.notificationService.error(error.error, true);
        console.error(error);
      }
    })
  }

  cleanForm() {
    this.form.reset();
  }

  onFileSelected(event:any){
    const file = event.target.files[0]

    if (file) {
      this.selectedFile = file
      this.form.patchValue({csv:file})
    }
  }

  uploadCsv() {
    if (!this.selectedFile){
      this.notificationService.warning('Debe subir un archivo CSV');
      return
    }

    this.loadingCsv = true;
    const formData = new FormData()
    formData.append('file', this.selectedFile)

    this.service.uploadCsv(formData, this.endpoint).subscribe({
      next: () => {
        this.loadingCsv = false;
        this.notificationService.success('CSV procesado correctamente')
        this.added.emit(true)
        this.cleanForm()
      },
      error: (err) => {
        this.loadingCsv = false;
        this.notificationService.error("Error procesando el CSV")
        console.log(err)
      }
    })
  }

}
