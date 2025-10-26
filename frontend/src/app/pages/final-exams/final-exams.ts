import { Component } from '@angular/core';
import { ExamList } from '../../components/exam-list/exam-list';
import { ExamForm } from '../../components/exam-form/exam-form';
import { ExamFormEdit } from '../../components/exam-form-edit/exam-form-edit';

@Component({
  selector: 'app-final-exams',
  imports: [ExamList, ExamForm, ExamFormEdit],
  templateUrl: './final-exams.html',
  styleUrl: './final-exams.css'
})
export class FinalExams {

}
