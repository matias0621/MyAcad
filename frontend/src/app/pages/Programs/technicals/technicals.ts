import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ProgramsList } from '../../../components/Programs/programs-list/programs-list';

@Component({
  selector: 'app-technicals',
  imports: [ReactiveFormsModule, ProgramsList],
  templateUrl: './technicals.html',
  styleUrl: './technicals.css'
})
export class Technicals {

}
