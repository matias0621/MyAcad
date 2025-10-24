import { Component, OnInit } from '@angular/core';
import Teacher from '../../../Models/Users/Teachers';
import { TeacherService } from '../../../Services/Users/teacher-service';
import { UserForm } from "../../../components/user-form/user-form";
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-teachers',
  imports: [UserForm, FormsModule],
  templateUrl: './teachers.html',
  styleUrl: './teachers.css'
})
export class Teachers implements OnInit {
  teachers !: Teacher[];
  search: string = '';
  timeout : any;

  constructor(
    private service: TeacherService
  ) { }

  ngOnInit(): void {
    this.getTeachers();
  }

  onSearch() {
    if (this.timeout) {
      clearTimeout(this.timeout);
    }

    this.timeout = setTimeout(() => {
      //Agarro el valor del input con ngModel y valido
      const value = this.search.trim();
      
      if (value.length === 0) {
        this.getTeachers();
        return;
      }

      //Si tiene solo números busca por legajo, si tiene letras busca por nombre completo
      if (/^[0-9]+$/.test(value)) {
        this.service.getByLegajo(value).subscribe({
          next: (data) => this.teachers = data,
          error: (err) => console.error(err)
        });
        return;
      } else if (/^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/.test(value)) {
        this.service.getByName(value).subscribe({
          next: (data) => this.teachers = data,
          error: (err) => console.error(err)
        });
        return;
      }
    }, 500)
  }

  getTeachers() {
    this.service.getTeachers().subscribe({
      next: (data) => { this.teachers = data },
      error: (error) => { console.error(error) }
    })
  }
  deleteTeacher(id: number) {
    this.service.deleteTeacher(id).subscribe({
      next: (data) => { this.getTeachers() },
      error: (error) => { console.error(error) }
    })
  }
}
