import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InscriptionToFinalExamService } from '../../Services/InscriptionToFinalExam/inscription-to-final-exam-service';
import { SubjectsService } from '../../Services/Subjects/subjects-service';
import { PostInscriptionToFinalExam } from '../../Models/InscriptionToFinalExam/InscriptionToFinalExam';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../../Services/notification/notification.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-inscription-to-exam-form',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './inscription-to-exam-form.html',
  styleUrl: './inscription-to-exam-form.css',
})
export class InscriptionToExamForm implements OnInit {
  @Output() inscriptionCreated = new EventEmitter<void>();
  form!: FormGroup;
  inscriptionDate!: FormControl;
  finalExamDate!: FormControl;
  subjectId: number | null = null;

  constructor(
    public inscriptionToFinalExamService: InscriptionToFinalExamService,
    public subjectsServices: SubjectsService,
    public router: Router,
    private notificationService : NotificationService
  ) {
    this.inscriptionDate = new FormControl('', [Validators.required]);
    this.finalExamDate = new FormControl('', [Validators.required]);

    this.form = new FormGroup({
      inscriptionDate: this.inscriptionDate,
      finalExamDate: this.finalExamDate,
    });
  }

  ngOnInit(): void {
    this.getInscription();
    this.getSubjects();
  }

  addToSubject(idSubject: number) {
    this.subjectId = idSubject;
    this.notificationService.info('Materia asignada a la inscripción.');
  }

  getInscription() {
    this.inscriptionToFinalExamService.getAllInscription().subscribe({
      next: (res) => {
        this.inscriptionToFinalExamService.inscriptionList = res;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  getSubjects() {
    this.subjectsServices.getAllSubject().subscribe({
      next: (res) => {
        this.subjectsServices.listSubject = [...res];
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  OnSubmit() {
    if (this.subjectId === null) {
      this.notificationService.error('Debe seleccionar una materia antes de inscribirse al examen.', true);
      return;
    }

    if (this.form.invalid) {
      this.notificationService.warning('Formulario inválido. Por favor, complete todos los campos correctamente.');
      this.form.markAllAsTouched();
      return;
    }
    
    const formatDate = (dateStr: string) => {
      const date = new Date(dateStr);
      const dd = String(date.getDate()).padStart(2, '0');
      const mm = String(date.getMonth() + 1).padStart(2, '0');
      const yyyy = date.getFullYear();
      const hh = String(date.getHours()).padStart(2, '0');
      const min = String(date.getMinutes()).padStart(2, '0');
      return `${dd}/${mm}/${yyyy} ${hh}:${min}`;
    };

    const inscription: PostInscriptionToFinalExam = {
      inscriptionDate: formatDate(this.inscriptionDate.value),
      finalExamDate: formatDate(this.finalExamDate.value),
      subjectsId: this.subjectId,
    };

    this.inscriptionToFinalExamService.postInscriptionToFinal(inscription).subscribe({
      next: (res) => {
        this.notificationService.success('Inscripcion al examen final realizada con exito');
        this.getInscription();
        this.form.reset();
        this.subjectId = null;
        this.form.markAsPristine();
        this.form.markAsUntouched();
        this.inscriptionCreated.emit();
      },
      error: (err) => {
        this.notificationService.error(err.error, true);
      },
    });
  }

  get selectedSubjectName(): string | null {
    if (this.subjectId === null) {
      return null;
    }

    const subject = this.subjectsServices.listSubject.find(sub => sub.id === this.subjectId);
    return subject ? subject.name : null;
  }
}