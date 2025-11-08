import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { UserService } from '../../../Services/Users/user-service';
import { FormsModule } from '@angular/forms';
import { UserForm } from '../user-form/user-form';
import { UserEditForm } from '../user-edit-form/user-edit-form';
import { NotificationService } from '../../../Services/notification/notification.service';

declare var bootstrap: any;

@Component({
  selector: 'app-user-list',
  imports: [FormsModule, UserForm, UserEditForm],
  templateUrl: './user-list.html',
  styleUrl: './user-list.css'
})
export class UserList implements OnInit {
  @Input()
  endpoint = ""
  @Output()
  user = new EventEmitter<any>;

  @ViewChild(UserEditForm) userEditForm!: UserEditForm;

  users !: any[]
  search: string = '';
  timeout: any;
  selectedUser: any = null;

  constructor(
    private service: UserService,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    this.getUsers();
  }

  onSearch() {
    if (this.timeout) {
      clearTimeout(this.timeout);
    }

    this.timeout = setTimeout(() => {
      //Agarro el valor del input con ngModel y valido
      const value = this.search.trim();

      if (value.length === 0) {
        this.getUsers();
        return;
      }

      //Si tiene solo números busca por legajo, si tiene letras busca por nombre completo
      if (/^[0-9]+$/.test(value)) {
        this.service.getByLegajo(value, this.endpoint).subscribe({
          next: (data) => this.users = data,
          error: (err) => console.error(err)
        });
        return;
      } else if (/^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/.test(value)) {
        this.service.getByName(value, this.endpoint).subscribe({
          next: (data) => { this.users = data },
          error: (err) => console.error(err)
        });
        return;
      }
    }, 500)
  }

  getUsers() {
    this.service.getUsers(this.endpoint).subscribe({
      next: (data) => { this.users = data },
      error: (error) => { console.error(error) }
    })
  }

  deleteUser(id: number) {
    this.notificationService.confirm(
      '¿Estás seguro de que deseas eliminar este usuario?',
      'Confirmar eliminación',
      'Eliminar',
      'Cancelar'
    ).then((confirmed) => {
      if (confirmed) {
        this.service.deleteUser(id, this.endpoint).subscribe({
          next: (data) => {
            this.notificationService.success('Usuario eliminado exitosamente');
            this.getUsers();
          },
          error: (error) => {
            this.notificationService.error('Error al eliminar el usuario. Por favor, intenta nuevamente', true);
            console.error(error);
          }
        });
      }
    });
  }


  // BAJA DEFINITIVA
  definitiveDeleteUser(id: number) {
    this.notificationService.confirm(
      '¿Estás seguro de que deseas eliminar permanentemente este usuario?',
      'Confirmar eliminación definitiva',
      'Eliminar',
      'Cancelar'
    ).then((confirmed) => {
      if (confirmed) {
        this.service.definitiveDeleteUser(id, this.endpoint).subscribe({
          next: (data) => {
            this.notificationService.success('Usuario eliminado exitosamente');
            this.getUsers();
          },
          error: (error) => {
            this.notificationService.error('Error al eliminar el usuario. Por favor, intenta nuevamente', true);
          }
        });
      }
    });
  }

  viewDisabled(user: any) {
    this.notificationService.confirm(
      `¿Deseas activar "${user.name}"?`,
      'Confirmar activación',
      'Activar',
      'Cancelar'
    ).then((confirmed) => {
      if (confirmed) {
        const updatedItem = { ...user, active: true };
        this.service.putUser(updatedItem, this.endpoint).subscribe({
          next: (response) => {
            this.notificationService.success(`${user.name} activado/a exitosamente`);
            this.getUsers();
          },
          error: (error) => {
            this.notificationService.error('Error al activar. Por favor, intenta nuevamente', true);
          }
        });
      }
    });
  }

  onUserSuccess(event: any) {
    this.getUsers();

    const modalElement = document.getElementById('modal-edit');
    const modal = bootstrap.Modal.getInstance(modalElement);

    modal.hide();
  }

  modifyUser(user: any) {
    this.user.emit(user);
  }
}
