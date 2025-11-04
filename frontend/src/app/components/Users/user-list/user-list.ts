import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { UserService } from '../../../Services/Users/user-service';
import { FormsModule } from '@angular/forms';
import { UserForm } from '../user-form/user-form';
import { UserEditForm } from '../user-edit-form/user-edit-form';

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
    private service: UserService
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
    this.service.deleteUser(id, this.endpoint).subscribe({
      next: (data) => { this.getUsers() },
      error: (error) => { console.error(error) }
    })
  }

  modifyUser(user : any){
    this.user.emit(user);
  }
}
