import { Component, OnInit } from '@angular/core';
import { ExamsService } from '../../../Services/Exams/exams-service';
import { AuthService } from '../../../Services/Auth/auth-service';
import { NotificationService } from '../../../Services/notification/notification.service';
import { Exams } from '../../../Models/Exam/Exam';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-teacher-grades-view',
  imports: [FormsModule],
  templateUrl: './teacher-grades-view.html',
  styleUrl: './teacher-grades-view.css'
})
export class TeacherGradesView implements OnInit {
  examsList: Exams[] = []
  loading: boolean = true
  editingExamId: number | null = null
  tempScore: number = 0

  constructor(
    private examsService: ExamsService,
    private authService: AuthService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.getExamsByTeacher()
  }

  getExamsByTeacher() {
    const token: any = this.authService.getDecodedToken()
    if (!token) {
      this.loading = false
      return
    }

    this.examsService.getExamsByTeacher(token.id).subscribe({
      next: (res) => {
        this.examsList = res
        this.loading = false
      },
      error: (err) => {
        console.error('Error fetching exams:', err)
        this.loading = false
      }
    })
  }

  startEdit(exam: Exams) {
    this.editingExamId = exam.id
    this.tempScore = exam.score
  }

  cancelEdit() {
    this.editingExamId = null
    this.tempScore = 0
  }

  saveScore(exam: Exams) {
    if (this.tempScore < 1 || this.tempScore > 10) {
      this.notificationService.warning('La nota debe estar entre 1 y 10')
      return
    }

    this.examsService.updateExamScore(exam.id, this.tempScore).subscribe({
      next: () => {
        exam.score = this.tempScore
        this.notificationService.success('Nota actualizada exitosamente')
        this.editingExamId = null
      },
      error: (err) => {
        this.notificationService.error('Error al actualizar la nota', true)
        console.error(err)
      }
    })
  }
}
