import { Component, OnInit } from '@angular/core';
import Manager from '../../../Models/Users/Manager';
import { ManagerService } from '../../../Services/Users/manager-service';
import { UserForm } from "../../../components/user-form/user-form";
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-managers',
  imports: [UserForm, FormsModule],
  templateUrl: './managers.html',
  styleUrl: './managers.css'
})
export class Managers implements OnInit {
  managers !: Manager[];
  search: string = '';
  timeout: any;

  constructor(
    private service: ManagerService
  ) { }

  ngOnInit(): void {
    this.getManagers();
  }

  onSearch() {
    if (this.timeout) {
      clearTimeout(this.timeout);
    }

    this.timeout = setTimeout(() => {
      const value = this.search.trim();
      if (value.length === 0) {
        this.getManagers();
        return;
      }


      //Si tiene solo números busca por legajo, si tiene letras busca por nombre completo
      if (/^[0-9]+$/.test(value)) {
        this.service.getByLegajo(value).subscribe({
          next: (data) => this.managers = data,
          error: (err) => console.error(err)
        });
        return;
      } else if (/^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/.test(value)) {
        this.service.getByName(value).subscribe({
          next: (data) => this.managers = data,
          error: (err) => console.error(err)
        });
        return;
      }
    }, 500)
  }

  getManagers() {
    this.service.getManagers().subscribe({
      next: (data) => { this.managers = data },
      error: (error) => { console.error(error) }
    })
  }

  deleteManager(id: number) {
    this.service.deleteManager(id).subscribe({
      next: (data) => { this.getManagers() },
      error: (error) => { console.error(error) }
    })
  }
}
