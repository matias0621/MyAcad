import { ActivatedRoute } from '@angular/router';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InscriptionToFinalExamService } from '../../Services/InscriptionToFinalExam/inscription-to-final-exam-service';
import { SubjectsService } from '../../Services/Subjects/subjects-service';
import { PostInscriptionToFinalExam } from '../../Models/InscriptionToFinalExam/InscriptionToFinalExam';
import { NotificationService } from '../../Services/notification/notification.service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-inscription-to-exam-form-edit',
  imports: [ReactiveFormsModule, DatePipe],
  templateUrl: './inscription-to-exam-form-edit.html',
  styleUrl: './inscription-to-exam-form-edit.css'
})
export class InscriptionToExamFormEdit {
  form!: FormGroup;
  inscriptionDate!: FormControl;
  finalExamDate!: FormControl;
  subjectId: number | null = null;
  idInscription!: string;
  selectedSubjectName: string | null = null;
  programName:FormControl


  constructor(
    public inscriptionToFinalExamService: InscriptionToFinalExamService,
    public subjectsServices: SubjectsService,
    public activatedRoute: ActivatedRoute,
    private notificationService:  NotificationService
  ) {
    this.inscriptionDate = new FormControl('', [Validators.required]);
    this.finalExamDate = new FormControl('', [Validators.required]);
    this.programName = new FormControl('',[Validators.required])

    this.idInscription = this.activatedRoute.snapshot.params['id']

    this.form = new FormGroup({
      inscriptionDate: this.inscriptionDate,
      finalExamDate: this.finalExamDate,
      program: this.programName
    });
  }

  ngOnInit(): void {
    if (this.idInscription) {
      this.getInscription();
    }
    this.getSubjects();
  }

  addToSubject(idSubject: number, subjectName: string) {
    this.subjectId = idSubject;
    this.selectedSubjectName = subjectName;
    this.notificationService.info(`Materia asignada: ${subjectName}`);
  }

  getInscription() {
    this.inscriptionToFinalExamService.getInscriptionById(parseInt(this.idInscription)).subscribe({
      next: (res) => {
        this.inscriptionDate.setValue(this.toDatetimeLocal(res.inscriptionDate));
        this.finalExamDate.setValue(this.toDatetimeLocal(res.finalExamDate));
        this.subjectId = res.subjects.id
        this.selectedSubjectName = res.subjects.name;
      },
      error: (err) => {
        console.error(err);
      },
    });
  }

  getSubjects() {
    this.subjectsServices.getAllSubject().subscribe({
      next: (res) => {
        this.subjectsServices.listSubject = [...res];
      },
      error: (err) => {
        console.error(err);
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
      program: this.programName.value,
      subjectsId: this.subjectId,
    };

    this.inscriptionToFinalExamService.putInscriptionToFinal(inscription, parseInt(this.idInscription)).subscribe({
      next: (res) => {
        this.notificationService.success("Inscripción editada exitosamente.");
        this.getInscription();
      },
      error: (err) => {
        this.notificationService.error(err.error, true);
        console.error(err);
      },
    });
  }

  private toDatetimeLocal(value: string): string {
    if (!value) {
      return '';
    }

    const parsed = new Date(value);
    if (!isNaN(parsed.getTime())) {
      const tzOffset = parsed.getTimezoneOffset();
      const adjusted = new Date(parsed.getTime() - tzOffset * 60000);
      return adjusted.toISOString().slice(0, 16);
    }

    const [datePart, timePart] = value.split(' ');
    if (datePart && timePart) {
      const [day, month, year] = datePart.split('/');
      const timePieces = timePart.split(':');
      const hour = timePieces[0];
      const minute = timePieces[1];
      if (day && month && year && hour && minute) {
        return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}T${hour.padStart(2, '0')}:${minute.padStart(2, '0')}`;
      }
    }

    return value;
  }
}
