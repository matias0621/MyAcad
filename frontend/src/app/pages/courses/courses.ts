import { Component, OnInit } from '@angular/core';
import { CareerService } from '../../Services/CareerService/career-service';
import Course from '../../Models/Users/Careers/Course';
import { ProgramsForm } from '../../components/programs-form/programs-form';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-courses',
  imports: [ProgramsForm, ReactiveFormsModule],
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
    if (confirm(`¿Deseas activar "${course.name}"?`)) {
      course.active = true;
      this.router.navigate(['/programs-edit-form', course.id]);
    }

  }
    toggleDisabledView() {
      this.showDisabled = !this.showDisabled;
    }
  
}
