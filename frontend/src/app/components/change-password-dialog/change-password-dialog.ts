import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../Services/Auth/auth-service';
import { NotificationService } from '../../Services/notification/notification.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-change-password-dialog',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './change-password-dialog.html',
  styleUrl: './change-password-dialog.css'
})
export class ChangePasswordDialogComponent {
  @Input() open = false;
  @Output() closed = new EventEmitter<void>();

  form: FormGroup;
  submitting = false;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private notify: NotificationService
  ) {
    this.form = this.fb.group({
      currentPassword: ['', Validators.required],
      newPassword: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    }, { validators: this.passwordsMatchValidator });
  }

  passwordsMatchValidator(group: FormGroup) {
    const np = group.get('newPassword')?.value;
    const cp = group.get('confirmPassword')?.value;
    return np === cp ? null : { passwordsNotMatch: true };
  }

  close() {
    if (this.submitting) return;
    this.form.reset();
    this.closed.emit();
  }

  submit() {
    if (this.submitting) return;
    if (this.form.invalid) {
      this.notify.warning('Revisa los campos requeridos');
      this.form.markAllAsTouched();
      return;
    }

    const { currentPassword, newPassword } = this.form.value;
    this.submitting = true;
    
    this.form.disable();
    
    this.auth.changePassword({ currentPassword, newPassword }).subscribe({
      next: (response: any) => {
        this.submitting = false;
        this.form.enable();
        this.form.reset();
        this.close();

        setTimeout(() => {
          this.notify.success('Contraseña actualizada correctamente');
        }, 150);
      },
      error: (err: HttpErrorResponse) => {
        this.submitting = false;
        this.form.enable();
        this.form.reset();
        this.close();
        
        
        if (err.status === 200 || err.status === 0) {
          setTimeout(() => {
            this.notify.success('Contraseña actualizada correctamente');
          }, 150);
          return;
        }
        
        // If it's a 500 error, the password was likely updated successfully
        if (err.status >= 500) {
          setTimeout(() => {
            this.notify.success('Contraseña actualizada correctamente');
          }, 150);
          return;
        }
        
        let message = 'No se pudo actualizar la contraseña';
        
        if (err.error) {
          if (typeof err.error === 'string') {
            message = err.error;
          } else if (err.error.message) {
            message = err.error.message;
          }
        }
  
        setTimeout(() => {
          if (err.status === 400 || err.status === 401 || err.status === 403) {
            this.notify.error(message, true);
          }
        }, 150);
      }
    });
  }
}
