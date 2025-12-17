import { Component, OnInit } from '@angular/core';
import { InscriptionToFinalExamService } from '../../../Services/InscriptionToFinalExam/inscription-to-final-exam-service';
import { AuthService } from '../../../Services/Auth/auth-service';
import { InscriptionToFinalExam } from '../../../Models/InscriptionToFinalExam/InscriptionToFinalExam';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-teacher-final-exams-view',
  imports: [DatePipe],
  templateUrl: './teacher-final-exams-view.html',
  styleUrl: './teacher-final-exams-view.css'
})
export class TeacherFinalExamsView implements OnInit {
  examsList: InscriptionToFinalExam[] = [];
  loading: boolean = true;
  expandedExams = new Set<number>();

  constructor(
    private inscriptionService: InscriptionToFinalExamService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.getFinalExamsByTeacher();
  }

  getFinalExamsByTeacher() {
    const token: any = this.authService.getDecodedToken();
    if (!token) {
      this.loading = false;
      return;
    }

    this.inscriptionService.getInscriptionsByTeacherId(token.id).subscribe({
      next: (res) => {
        this.examsList = res;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching final exams:', err);
        this.loading = false;
      }
    });
  }

  toggleStudents(examId: number) {
    if (this.expandedExams.has(examId)) {
      this.expandedExams.delete(examId);
    } else {
      this.expandedExams.add(examId);
    }
  }

  isExpanded(examId: number) {
    return this.expandedExams.has(examId);
  }

  formatDate(date: string): string {
    if (!date) return '-';
    const dateObj = new Date(date);
    return dateObj.toLocaleString('es-AR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}

