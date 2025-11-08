import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ExamFinal } from '../../../Models/Final-Exam/FinalExam';
import { Exam } from '../../../Models/Exam/Exam';
import { ExamsService } from '../../../Services/Exams/exams-service';
import Program from '../../../Models/Program/Program';
import { ExamForm } from '../exam-form/exam-form';
import { ExamFormEdit } from '../exam-form-edit/exam-form-edit';
declare var bootstrap: any;

@Component({
  selector: 'app-exam-list',
  imports: [ExamForm, ExamFormEdit],
  templateUrl: './exam-list.html',
  styleUrl: './exam-list.css'
})
export class ExamList implements OnInit {
  @Input()
  endpoint = ""
  @Output()
  exam = new EventEmitter<Exam | ExamFinal>;

  programs: Program[] = [];
  exams !: Exam[] | ExamFinal[]
  timeout: any;
  selectedExam ?: Exam;

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

  modifyExam(exam: ExamFinal | Exam) {
    this.exam.emit(exam);
  }

  onExamSuccess(event: any) {
    this.getExams();

    const modalElement = document.getElementById('modal-edit');
    const modal = bootstrap.Modal.getInstance(modalElement);

    modal.hide();
  }

}
