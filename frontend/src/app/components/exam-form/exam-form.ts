import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../Services/Users/user-service';
import { Exam, PostExam } from '../../Models/Exam/Exam';
import { ExamFinal, PostExamFinal } from '../../Models/Final-Exam/FinalExam';
import { ExamsService } from '../../Services/Exams/exams-service';
import { SubjectsService } from '../../Services/Subjects/subjects-service';

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
  added = new EventEmitter<Exam[] | ExamFinal[]>; 

  form !: FormGroup;

  idSubjects:number | null = null


  constructor(
    private service: ExamsService,
    public subjectsService: SubjectsService,
    private fb: FormBuilder
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
      alert("Un examen debe tener una materia asignada")
      return
    }

    const examLoad:PostExam | PostExamFinal = {
      score: this.form.value,
      subjectsId: this.idSubjects
    }


    this.service.postExam(this.endpoint , examLoad).subscribe({
      next: (data) => {
        console.log('Examen creado exitosamente:');
        this.form.reset();
        this.service.getAllExams(this.endpoint).subscribe({
          next: (data) => { this.added.emit(data) },
          error: (error) => { console.error(error) }
        })
      },
      error: (error) => { console.error(error) }
    })
  }

  addSubjectsToExam(id:number){
    this.idSubjects = id
    alert("Se añadio esta materia al examen")
  }

  cleanForm() {
    this.form.reset();
  }
}
