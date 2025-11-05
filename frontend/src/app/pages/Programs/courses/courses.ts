import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ProgramsList } from '../../../components/Programs/programs-list/programs-list';

@Component({
  selector: 'app-courses',
  imports: [ReactiveFormsModule, ProgramsList],
  templateUrl: './courses.html',
  styleUrl: './courses.css'
})
export class Courses {
  
}
