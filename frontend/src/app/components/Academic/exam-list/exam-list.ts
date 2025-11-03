import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UserService } from '../../../Services/Users/user-service';
import { ExamFinal } from '../../../Models/Final-Exam/FinalExam';
import { Exam } from '../../../Models/Exam/Exam';
import { ExamsService } from '../../../Services/Exams/exams-service';

@Component({
  selector: 'app-exam-list',
  imports: [],
  templateUrl: './exam-list.html',
  styleUrl: './exam-list.css'
})
export class ExamList implements OnInit {
  @Input()
  endpoint = ""
  @Output()
  exam = new EventEmitter<Exam | ExamFinal>;

  exams !: Exam[] | ExamFinal[]
  timeout: any;

  constructor(
    private service: ExamsService
  ) { }

  ngOnInit(): void {
    this.getExams();
  }

  getExams() {
    this.service.getAllExams(this.endpoint).subscribe({
      next: (data) => { this.exams = data },
      error: (error) => { console.error(error) }
    })
  }

  deleteExam(id: number) {
    this.service.deleteExam(this.endpoint, id).subscribe({
      next: (data) => { this.getExams() },
      error: (error) => { console.error(error) }
    })
  }

  modifyExam(exam : ExamFinal | Exam){
    this.exam.emit(exam);
  }
}
