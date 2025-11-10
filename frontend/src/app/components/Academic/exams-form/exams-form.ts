import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ExamsService } from '../../../Services/Exams/exams-service';
import { SubjectsService } from '../../../Services/Subjects/subjects-service';
import { NotificationService } from '../../../Services/notification/notification.service';
import { ExamsPost } from '../../../Models/Exam/Exam';

@Component({
  selector: 'app-exams-form',
  imports: [ReactiveFormsModule],
  templateUrl: './exams-form.html',
  styleUrl: './exams-form.css',
})
export class ExamsForm {
  form!: FormGroup;
  score!: FormControl
  legajo!: FormControl
  examType!: FormControl

  idSubjects: number | null = null;

  constructor(
    private service: ExamsService,
    public subjectsService: SubjectsService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {

    this.legajo = new FormControl("", [Validators.required])
    this.examType = new FormControl("", [Validators.required])
    this.score = new FormControl("", [Validators.required])

    this.form = new FormGroup({
      legajo: this.legajo,
      score: this.score,
      examType: this.examType
    });

    this.subjectsService.getAllSubject().subscribe({
      next: (res) => {
        this.subjectsService.listSubject = [...res];
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  OnSubmit() {
    if (this.idSubjects == null) {
      this.notificationService.warning('Un examen debe tener una materia asignada', true);
      return;
    }

    if (this.form.invalid) {
      this.notificationService.warning('Formulario inválido. Por favor, complete todos los campos correctamente.');
      this.form.markAllAsTouched();
      return;
    }

    const examLoad: ExamsPost = {
      score: this.score.value,
      examType: this.examType.value,
      legajoStudent: this.legajo.value,
      subjectId: this.idSubjects,
    };

    this.service.postExam(examLoad).subscribe({
      next: (data) => {
        this.notificationService.success('Parcial agregado exitosamente');
        this.form.reset();
      },
      error: (error) => {
        this.notificationService.error(error.error, true);
        console.error(error);
      },
    });
  }

  addSubjectsToExam(id: number) {
    this.idSubjects = id;
    this.notificationService.info('Se añadió esta materia al examen');
  }

  cleanForm() {
    this.form.reset();
  }
}
