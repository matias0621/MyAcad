import { ActivatedRoute } from '@angular/router';
import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InscriptionToFinalExamService } from '../../Services/InscriptionToFinalExam/inscription-to-final-exam-service';
import { SubjectsService } from '../../Services/Subjects/subjects-service';
import { InscriptionToFinalExam, PostInscriptionToFinalExam } from '../../Models/InscriptionToFinalExam/InscriptionToFinalExam';
import { NotificationService } from '../../Services/notification/notification.service';

@Component({
  selector: 'app-inscription-to-exam-form-edit',
  imports: [ReactiveFormsModule],
  templateUrl: './inscription-to-exam-form-edit.html',
  styleUrl: './inscription-to-exam-form-edit.css'
})
export class InscriptionToExamFormEdit implements OnInit, OnChanges {
  @Input() inscription: InscriptionToFinalExam | null = null;
  @Output() updated = new EventEmitter<void>();

  form!: FormGroup;
  inscriptionDate!: FormControl;
  finalExamDate!: FormControl;
  teacherLegajo!:FormControl;
  subjectId: number | null = null;
  idInscription: number | null = null;
  programName:FormControl

  constructor(
    public inscriptionToFinalExamService: InscriptionToFinalExamService,
    public subjectsServices: SubjectsService,
    public activatedRoute: ActivatedRoute,
    private notificationService:  NotificationService
  ) {
    this.inscriptionDate = new FormControl('', [Validators.required]);
    this.finalExamDate = new FormControl('', [Validators.required]);
    this.programName = new FormControl('', [Validators.required])
    this.teacherLegajo = new FormControl('', [Validators.required]);

    this.idInscription = this.activatedRoute.snapshot.params['id']

    this.form = new FormGroup({
      inscriptionDate: this.inscriptionDate,
      finalExamDate: this.finalExamDate,
      program: this.programName,
      teacherLegajo:this.teacherLegajo
    });
  }

  ngOnInit(): void {
    this.getSubjects();

    if (this.idInscription) {
      this.loadInscriptionById(this.idInscription);
    } else if (this.inscription) {
      this.applyInscription(this.inscription);
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['inscription'] && changes['inscription'].currentValue) {
      this.applyInscription(changes['inscription'].currentValue);
    }
  }

  addToSubject(idSubject: number) {
    this.subjectId = idSubject;
    this.notificationService.info('Materia asignada a la inscripción.');
  }

  private loadInscriptionById(id: number) {
    this.inscriptionToFinalExamService.getInscriptionById(id).subscribe({
      next: (res) => {
        this.inscription = res;
        this.applyInscription(res);
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
      this.notificationService.warning('Añada de qué materia es el examen');
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
      teacherLegajo: this.teacherLegajo.value,
      subjectsId: this.subjectId,
    };

    const targetId = this.idInscription ?? this.inscription?.id;

    if (!targetId) {
      this.notificationService.error('No se encontró la inscripción a actualizar.', true);
      return;
    }

    this.inscriptionToFinalExamService.putInscriptionToFinal(inscription, targetId).subscribe({
      next: () => {
        this.notificationService.success("Inscripción editada exitosamente.");
        this.updated.emit();
        this.loadInscriptionById(targetId);
      },
      error: (err) => {
        this.notificationService.error(err.body, true);
        console.error(err);
      },
    });
  }

  get selectedSubjectName(): string | null {
    if (this.subjectId === null) {
      return null;
    }

    const selectedSubject = this.subjectsServices.listSubject?.find((subject) => subject.id === this.subjectId);
    return selectedSubject ? selectedSubject.name : null;
  }

  private applyInscription(inscription: InscriptionToFinalExam) {
    this.idInscription = inscription.id;
    this.subjectId = inscription.subjects?.id ?? null;

    const inscriptionDateValue = this.normalizeDateForInput(inscription.inscriptionDate);
    const finalExamDateValue = this.normalizeDateForInput(inscription.finalExamDate);

    this.inscriptionDate.setValue(inscriptionDateValue);
    this.finalExamDate.setValue(finalExamDateValue);

    this.form.markAsPristine();
    this.form.markAsUntouched();
  }

  private normalizeDateForInput(value: string): string {
    if (!value) {
      return '';
    }

    const parsedISO = new Date(value);
    if (!Number.isNaN(parsedISO.getTime())) {
      return this.toDateTimeLocal(parsedISO);
    }

    const match = value.match(/^(\d{2})\/(\d{2})\/(\d{4})\s+(\d{2}):(\d{2})$/);
    if (match) {
      const [, dd, mm, yyyy, hh, min] = match;
      return `${yyyy}-${mm}-${dd}T${hh}:${min}`;
    }

    return value;
  }

  private toDateTimeLocal(date: Date): string {
    const pad = (n: number) => n.toString().padStart(2, '0');
    const yyyy = date.getFullYear();
    const mm = pad(date.getMonth() + 1);
    const dd = pad(date.getDate());
    const hh = pad(date.getHours());
    const min = pad(date.getMinutes());
    return `${yyyy}-${mm}-${dd}T${hh}:${min}`;
  }
}