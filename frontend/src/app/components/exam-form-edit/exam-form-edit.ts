import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../Services/Users/user-service';
import { SubjectsService } from '../../Services/Subjects/subjects-service';
import { Exam } from '../../Models/Exam/Exam';
import { ExamFinal } from '../../Models/Final-Exam/FinalExam';
import { ActivatedRoute } from '@angular/router';

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
  added = new EventEmitter<Exam[] | ExamFinal[]>;

  examId !: number
  form !: FormGroup;
  idSubjects:number | null = null


  constructor(
    private service: UserService,
    public subjectsService: SubjectsService,
    private fb: FormBuilder,
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
    const modifiedUser = { id: this.examId, ...this.form.value }
    this.service.putUser(modifiedUser, this.endpoint).subscribe({
      next: (data) => {
        console.log('Usuario modificado exitosamente:');
        this.form.reset();
        this.service.getUsers(this.endpoint).subscribe({
          next: (data) => { this.added.emit(data) },
          error: (error) => { console.error(error) }
        })
      },
      error: (error) => { console.error(error) }
    })
  }

  loadData(examenData: Exam | ExamFinal) {
    this.examId = examenData.id

    if (examenData.subject != null){
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

  addSubjectsToExam(id:number){
    this.idSubjects = id
    alert("Se añadio esta materia al examen")
  }

}
