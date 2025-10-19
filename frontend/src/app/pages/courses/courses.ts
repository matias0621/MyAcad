import { Component, OnInit } from '@angular/core';
import Career from '../../Models/Users/Careers/Career';
import { CareerService } from '../../Services/CareerService/career-service';
import { ProgramsForm } from "../../components/programs-form/programs-form";

@Component({
  selector: 'app-courses',
  imports: [ProgramsForm],
  templateUrl: './courses.html',
  styleUrl: './courses.css'
})
export class Courses implements OnInit{
  courses !: Career[];

  constructor(
    private service: CareerService
  ) { }

  ngOnInit(): void {
    this.getCourses();
  }

  getCourses() {
    this.service.getCareers().subscribe({
      next: (data) => { this.courses = data },
      error: (error) => { console.error(error) }
    })
  }

  deleteCourse(id: string) {
    this.service.deleteCareer(id).subscribe({
      next: (data) => { this.getCourses() },
      error: (error) => { console.error(error) }
    })
  }

}
