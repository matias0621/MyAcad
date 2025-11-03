import { Component, OnInit } from '@angular/core';
import { CareerService } from '../../../Services/CareerService/career-service';
import Career from '../../../Models/Users/Careers/Career';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { ProgramsEditForm } from "../../../components/Programs/programs-edit-form/programs-edit-form";
import { ProgramsForm } from '../../../components/Programs/programs-form/programs-form';
import { ProgramsList } from '../../../components/Programs/programs-list/programs-list';

@Component({
  selector: 'app-engineerings',
  imports: [ProgramsForm, ReactiveFormsModule, ProgramsEditForm, ProgramsList],
  templateUrl: './engineerings.html',
  styleUrl: './engineerings.css'
})
export class Engineerings {

}


