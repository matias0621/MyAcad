import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { SubjectsService } from '../../../Services/Subjects/subjects-service';
import { Exam, PostExam } from '../../../Models/Exam/Exam';
import { ExamFinal, PostExamFinal } from '../../../Models/Final-Exam/FinalExam';
import { ExamsService } from '../../../Services/Exams/exams-service';
import { NotificationService } from '../../../Services/notification/notification.service';

@Component({
  selector: 'app-exam-form-edit',
  imports: [ReactiveFormsModule],
  templateUrl: './exam-form-edit.html',
  styleUrl: './exam-form-edit.css'
})
export class ExamFormEdit {

  @Input()
  endpoint: string = "";

  @Output()
  added = new EventEmitter<any>;

  examId !: number
  form !: FormGroup;
  idSubjects: number | null = null


  constructor(
    private service: ExamsService,
    public subjectsService: SubjectsService,
    private fb: FormBuilder,
    private notificationService: NotificationService
  ) { }


  ngOnInit(): void {
    this.form = this.fb.group({
      score: ['', [Validators.required, Validators.min(1), Validators.max(100)]],
    })

    this.subjectsService.getAllSubject().subscribe({
      next: (res) => {
        this.subjectsService.listSubject = [...res]
      },
      error: (err) => {
        console.log(err)
      }
    })
  }


  OnSubmit() {

    if (this.idSubjects == null) {
      this.notificationService.warning("Un examen debe tener una materia asignada", true);
      return
    }

    const examLoad: PostExam | PostExamFinal = {
      score: this.form.value.score,
      subjectId: this.idSubjects
    }

    this.service.putExam(this.endpoint, examLoad, this.examId).subscribe({
      next: (data) => {
        this.notificationService.success('Parcial modificado exitosamente');
        this.form.reset();
        this.service.getAllExams(this.endpoint).subscribe({
          next: (data) => { this.added.emit(true) },
          error: (error) => { console.error(error) }
        })
      },
      error: (error) => {
        this.notificationService.error('Error al modificar el parcial');
        console.error(error)
      }
    })
  }

  loadData(examenData: Exam | ExamFinal) {
    this.examId = examenData.id

    if (examenData.subject != null) {
      this.idSubjects = examenData.subject.id
    }

    const mappedData = {
      score: examenData.score
    }

    this.form.patchValue(mappedData)
  }

  cleanForm() {
    this.form.reset();
  }

  addSubjectsToExam(id: number) {
    this.idSubjects = id
    this.notificationService.info("Se añadió esta materia al examen");
  }

}
