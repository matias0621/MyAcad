import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ProgramsList } from '../../../components/Programs/programs-list/programs-list';

@Component({
  selector: 'app-engineerings',
  imports: [ReactiveFormsModule, ProgramsList],
  templateUrl: './engineerings.html',
  styleUrl: './engineerings.css'
})
export class Engineerings {

}


