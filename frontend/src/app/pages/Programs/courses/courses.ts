import { Component, OnInit } from '@angular/core';
import { CareerService } from '../../../Services/CareerService/career-service';
import Course from '../../../Models/Users/Careers/Course';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ProgramsEditForm } from "../../../components/Programs/programs-edit-form/programs-edit-form";
import { ProgramsForm } from '../../../components/Programs/programs-form/programs-form';
import { ProgramsList } from '../../../components/Programs/programs-list/programs-list';

@Component({
  selector: 'app-courses',
  imports: [ProgramsForm, ReactiveFormsModule, ProgramsEditForm, ProgramsList],
  templateUrl: './courses.html',
  styleUrl: './courses.css'
})
export class Courses {
  
}
