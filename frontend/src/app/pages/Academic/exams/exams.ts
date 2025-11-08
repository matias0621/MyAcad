import { Component } from '@angular/core';
import { ExamList } from '../../../components/Academic/exam-list/exam-list';

@Component({
  selector: 'app-exams',
  imports: [ExamList],
  templateUrl: './exams.html',
  styleUrl: './exams.css'
})
export class Exams {

}
