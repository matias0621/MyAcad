import { Component, OnInit } from '@angular/core';
import { CareerService } from '../../../Services/CareerService/career-service';
import Career from '../../../Models/Users/Careers/Career';
import { Router } from '@angular/router';
import Technical from '../../../Models/Users/Careers/Technical';
import { ProgramsForm } from '../../../components/programs-form/programs-form';
import { ReactiveFormsModule } from '@angular/forms';
import { ProgramsEditForm } from "../../../components/programs-edit-form/programs-edit-form";
import { ProgramsList } from "../../../components/programs-list/programs-list";

@Component({
  selector: 'app-technicals',
  imports: [ProgramsForm, ReactiveFormsModule, ProgramsEditForm, ProgramsList],
  templateUrl: './technicals.html',
  styleUrl: './technicals.css'
})
export class Technicals {

}
