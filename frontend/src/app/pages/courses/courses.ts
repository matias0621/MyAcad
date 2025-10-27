import { Component, OnInit } from '@angular/core';
import { CareerService } from '../../Services/CareerService/career-service';
import Course from '../../Models/Users/Careers/Course';
import { ProgramsForm } from '../../components/programs-form/programs-form';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ProgramsEditForm } from "../../components/programs-edit-form/programs-edit-form";
import { ProgramsList } from "../../components/programs-list/programs-list";

@Component({
  selector: 'app-courses',
  imports: [ProgramsForm, ReactiveFormsModule, ProgramsEditForm, ProgramsList],
  templateUrl: './courses.html',
  styleUrl: './courses.css'
})
export class Courses implements OnInit {
  courses !: Course[];
  showDisabled = false;

  constructor(
    private service: CareerService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.getCourses();
  }

  getCourses() {
    this.service.getCareers('courses').subscribe({
      next: (data) => {
        this.courses = data;
      },
      error: (error) => {
        console.error(error);
      }
    })
  }

  deleteCourse(id: number) {
    this.service.deleteCareer(id, 'courses').subscribe({
      next: (data) => { this.getCourses() },
      error: (error) => { console.error(error) }
    })
  }

  viewDisabled(course: Course) {
      console.log('Curso a activar:', course);
      if (confirm(`¿Deseas activar "${course.name}"?`)) {
        const updatedCourse = { ...course, active: true };
        console.log('Enviando al servidor:', updatedCourse);
        this.service.updateByEndpoint(updatedCourse, 'courses').subscribe({
          next: (response) => {
            console.log('Curso activado exitosamente', response);
            this.getCourses();
          },
          error: (error) => {
            console.error('Error al activar el curso:', error);
          }
        });
      }
    }
    toggleDisabledView() {
      this.showDisabled = !this.showDisabled;
    }
  
}
