import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../../Services/Users/user-service';
import { Exam, PostExam } from '../../../Models/Exam/Exam';
import { ExamFinal, PostExamFinal } from '../../../Models/Final-Exam/FinalExam';
import { ExamsService } from '../../../Services/Exams/exams-service';
import { SubjectsService } from '../../../Services/Subjects/subjects-service';
import { NotificationService } from '../../../Services/notification/notification.service';

@Component({
  selector: 'app-exam-form',
  imports: [ReactiveFormsModule],
  templateUrl: './exam-form.html',
  styleUrl: './exam-form.css'
})
export class ExamForm {
  @Input()
  endpoint: string = "";

  @Output()
  // Este output puede recibir tanto examenes finales como examenes normales
  added = new EventEmitter<any>; 

  form !: FormGroup;

  idSubjects:number | null = null


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

    if (this.idSubjects == null){
      this.notificationService.warning("Un examen debe tener una materia asignada", true);
      return
    }

    const examLoad:PostExam | PostExamFinal = {
      score: this.form.value.score,
      subjectId: this.idSubjects
    }

    this.service.postExam(this.endpoint , examLoad).subscribe({
      next: (data) => {
        this.notificationService.success('Parcial agregado exitosamente');
        this.form.reset();
        this.service.getAllExams(this.endpoint).subscribe({
          next: (data) => { this.added.emit(true) },
          error: (error) => { console.error(error) }
        })
      },
      error: (error) => { 
        this.notificationService.error(error.error, true);
        console.error(error) }
    })
  }

  addSubjectsToExam(id:number){
    this.idSubjects = id
    this.notificationService.info("Se añadió esta materia al examen");
  }

  cleanForm() {
    this.form.reset();
  }
}
