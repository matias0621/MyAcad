import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import Student from '../../../Models/Users/Student';
import { StudentService } from '../../../Services/Users/student-service';
import { UserForm } from "../../../components/user-form/user-form";

@Component({
  selector: 'app-students',
  imports: [ReactiveFormsModule, UserForm, FormsModule],
  templateUrl: './students.html',
  styleUrl: './students.css'
})
export class Students implements OnInit {
  students !: Student[];
  search: string = '';
  timeout: any;

  constructor(
    private service: StudentService
  ) { }
  ngOnInit(): void {
    this.getStudents();
  }

  onSearch() {
    if (this.timeout) {
      clearTimeout(this.timeout);
    }

    this.timeout = setTimeout(() => {
      const value = this.search.trim();
      if (value.length === 0) {
        this.getStudents();
        return;
      }


      //Si tiene solo números busca por legajo, si tiene letras busca por nombre completo
      if (/^[0-9]+$/.test(value)) {
        this.service.getByLegajo(value).subscribe({
          next: (data) => this.students = data,
          error: (err) => console.error(err)
        });
        return;
      } else if (/^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/.test(value)) {
        this.service.getByName(value).subscribe({
          next: (data) => this.students = data,
          error: (err) => console.error(err)
        });
        return;
      }
    }, 500)
  }

  getStudents() {
    this.service.getStudents().subscribe({
      next: (data) => { this.students = data },
      error: (error) => { console.error(error) }
    })
  }

  deleteStudent(id: number) {
    this.service.deleteStudent(id).subscribe({
      next: (data) => { this.getStudents() },
      error: (error) => { console.error(error) }
    })
  }
}
