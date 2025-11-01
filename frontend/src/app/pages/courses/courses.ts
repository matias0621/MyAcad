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
export class Courses {
  
}
