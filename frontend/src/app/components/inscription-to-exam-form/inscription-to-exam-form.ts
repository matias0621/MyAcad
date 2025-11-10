import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InscriptionToFinalExamService } from '../../Services/InscriptionToFinalExam/inscription-to-final-exam-service';
import { SubjectsService } from '../../Services/Subjects/subjects-service';
import { PostInscriptionToFinalExam } from '../../Models/InscriptionToFinalExam/InscriptionToFinalExam';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../../Services/notification/notification.service';

@Component({
  selector: 'app-inscription-to-exam-form',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './inscription-to-exam-form.html',
  styleUrl: './inscription-to-exam-form.css',
})
export class InscriptionToExamForm implements OnInit {
  form!: FormGroup;
  inscriptionDate!: FormControl;
  finalExamDate!: FormControl;
  subjectId: number | null = null;
  selectedSubjectName: string | null = null;

  constructor(
    public inscriptionToFinalExamService: InscriptionToFinalExamService,
    public subjectsServices: SubjectsService,
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

  addToSubject(idSubject: number, subjectName: string) {
    this.subjectId = idSubject;
    this.selectedSubjectName = subjectName;
    this.notificationService.info(`Materia asignada: ${subjectName}`);
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
      this.notificationService.warning('Seleccioná una materia antes de guardar la inscripción.');
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
        this.selectedSubjectName = null;
      },
      error: (err) => {
        this.notificationService.error(err.error, true);
      },
    });
  }
}
