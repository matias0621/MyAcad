import { Component, OnInit } from '@angular/core';
import { ProgramService } from '../../Services/program-service';
import Program from '../../Models/Program/Program';
import { Router } from '@angular/router';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NotificationService } from '../../Services/notification/notification.service';

@Component({
  selector: 'app-show-career-for-register',
  imports: [ReactiveFormsModule],
  templateUrl: './show-career-for-register.html',
  styleUrl: './show-career-for-register.css',
})
export class ShowCareerForRegister implements OnInit {
  listProgram!: Program[];
  form!: FormGroup;
  legajo!: FormControl;
  programName!: string;

  constructor(
    private program: ProgramService,
    private router: Router,
    private notification: NotificationService
  ) {
    this.legajo = new FormControl('', [Validators.required]);
    this.form = new FormGroup({
      legajo: this.legajo,
    });
  }

  ngOnInit(): void {
    this.program.getPrograms().subscribe({
      next: (res) => {
        this.listProgram = res;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  addToStudent() {
    this.program.registerStudent(this.programName, this.legajo.value).subscribe({
      next: (res) => {
        this.notification.success('Se registro el alumno correctamente en la carrera');
      },
      error: (err) => {
        this.notification.error('Hubo un error', true);
        console.log(err);
      },
    });
  }

  addToTeacher() {
    this.program.registerTeacher(this.programName, this.legajo.value).subscribe({
      next: (res) => {
        this.notification.success('Se registro el profesor correctamente en la carrera');
      },
      error: (err) => {
        this.notification.error('Hubo un error', true);
        console.log(err);
      },
    });
  }

  setName(name: string) {
    this.programName = name;
  }
}
