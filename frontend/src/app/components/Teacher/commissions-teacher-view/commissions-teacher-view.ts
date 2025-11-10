import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommissionService } from '../../../Services/Commission/commission-service';
import { AuthService } from '../../../Services/Auth/auth-service';
import Commission from '../../../Models/Commission/commission';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import Student from '../../../Models/Users/Student';
import { ExamsService } from '../../../Services/Exams/exams-service';
import { Exams, ExamsPost } from '../../../Models/Exam/Exam';
import { NotificationService } from '../../../Services/notification/notification.service';

interface ExamGrade {
  id?: number
  type: 'EXAM' | 'FINAL_EXAM' | 'MAKEUP_EXAM'
  score: number
  date?: string
  subjectId?: number
}

@Component({
  selector: 'app-commissions-teacher-view',
  imports: [FormsModule, DatePipe],
  templateUrl: './commissions-teacher-view.html',
  styleUrl: './commissions-teacher-view.css'
})
export class CommissionsTeacherView implements OnInit {
  programId!: number
  commissionList: Commission[] = []
  loading: boolean = true
  expandedCommissions = new Set<number>()

  // Modal de notas
  showGradesModal = false
  selectedStudent: Student | null = null
  selectedCommission: Commission | null = null
  studentGrades: ExamGrade[] = []
  newGrade: ExamGrade = { type: 'EXAM', score: 0, subjectId: 0 }
  editingGradeId: number | null = null
  loadingGrades = false

  constructor(
    private commissionService: CommissionService,
    private activatedRoute: ActivatedRoute,
    private authService: AuthService,
    private examsService: ExamsService,
    private notificationService: NotificationService
  ) {
    this.programId = Number(this.activatedRoute.snapshot.params['programId'])
  }

  ngOnInit(): void {
    this.getCommissionsByProgramAndTeacher()
  }

  getCommissionsByProgramAndTeacher() {
    const token: any = this.authService.getDecodedToken()
    if (!token) {
      this.loading = false
      return
    }

    this.commissionService.getCommissionsByProgramAndTeacher(this.programId, token.id).subscribe({
      next: (res) => {
        const normalized = (res as any[]).map((c: any) => this.normalizeCommission(c))
        // Eliminar duplicados por ID
        this.commissionList = normalized.filter((commission, index, self) =>
          index === self.findIndex((c) => c.id === commission.id)
        )
        this.loading = false
      },
      error: (err) => {
        console.error('Error fetching commissions:', err)
        this.loading = false
      }
    })
  }

  toggleStudents(commissionId: number) {
    if (this.expandedCommissions.has(commissionId)) {
      this.expandedCommissions.delete(commissionId)
    } else {
      this.expandedCommissions.add(commissionId)
    }
  }

  isExpanded(commissionId: number) {
    return this.expandedCommissions.has(commissionId)
  }

  private normalizeCommission(c: any): Commission {
    const programName = typeof c.program === 'object' ? c.program?.name : c.program
    return {
      id: c.id,
      number: c.number,
      capacity: c.capacity ?? 0,
      students: c.students ?? [],
      subjects: c.subjects ?? [],
      program: programName ?? '',
      active: c.active ?? true
    }
  }

  // Gestión de modal de notas
  openGradesModal(student: Student, commission: Commission) {
    this.selectedStudent = student
    this.selectedCommission = commission
    this.loadStudentGrades(student.id)
    this.showGradesModal = true
  }

  closeGradesModal() {
    this.showGradesModal = false
    this.selectedStudent = null
    this.selectedCommission = null
    this.studentGrades = []
    this.newGrade = { type: 'EXAM', score: 0, subjectId: 0 }
    this.editingGradeId = null
  }

  loadStudentGrades(studentId: number) {
    this.loadingGrades = true
    this.examsService.getExamsByStudents(studentId).subscribe({
      next: (exams: Exams[]) => {
        this.studentGrades = exams.map(exam => ({
          id: exam.id,
          type: exam.examType as 'EXAM' | 'FINAL_EXAM' | 'MAKEUP_EXAM',
          score: exam.score,
          subjectId: exam.subject?.id,
          date: undefined
        }))
        this.loadingGrades = false
      },
      error: (err) => {
        console.error('Error loading grades:', err)
        this.studentGrades = []
        this.loadingGrades = false
      }
    })
  }

  addGrade() {
    if (this.newGrade.score < 0 || this.newGrade.score > 10) {
      this.notificationService.showToast('La nota debe estar entre 0 y 10', 'error')
      return
    }

    if (!this.selectedStudent) {
      this.notificationService.showToast('No hay estudiante seleccionado', 'error')
      return
    }

    if (!this.newGrade.subjectId) {
      this.notificationService.showToast('Debe seleccionar una materia', 'error')
      return
    }

    if (this.editingGradeId) {
      // Editar nota existente
      const examData: ExamsPost = {
        score: this.newGrade.score,
        examType: this.newGrade.type,
        subjectId: Number(this.newGrade.subjectId),
        legajoStudent: this.selectedStudent.legajo
      }

      this.examsService.putExam(this.editingGradeId, examData).subscribe({
        next: () => {
          this.notificationService.showToast('Nota actualizada exitosamente', 'success')
          this.loadStudentGrades(this.selectedStudent!.id)
          this.newGrade = { type: 'EXAM', score: 0, subjectId: 0 }
          this.editingGradeId = null
        },
        error: (err) => {
          console.error('Error updating grade:', err)
          this.notificationService.showToast('Error al actualizar la nota', 'error')
        }
      })
    } else {
      // Agregar nueva nota
      const examData: ExamsPost = {
        score: this.newGrade.score,
        examType: this.newGrade.type,
        subjectId: Number(this.newGrade.subjectId),
        legajoStudent: this.selectedStudent.legajo
      }

      this.examsService.postExam(examData).subscribe({
        next: () => {
          this.notificationService.showToast('Nota agregada exitosamente', 'success')
          this.loadStudentGrades(this.selectedStudent!.id)
          this.newGrade = { type: 'EXAM', score: 0, subjectId: 0 }
        },
        error: (err) => {
          console.error('Error adding grade:', err)
          console.error('Exam data sent:', examData)
          console.error('Error details:', err.error)
          this.notificationService.showToast(`Error al agregar la nota: ${err.error?.message || 'Sin permisos'}`, 'error')
        }
      })
    }
  }

  editGrade(grade: ExamGrade) {
    this.newGrade = { 
      type: grade.type, 
      score: grade.score,
      subjectId: grade.subjectId || 0
    }
    this.editingGradeId = grade.id || null
  }

  deleteGrade(gradeId: number) {
    this.notificationService.confirm(
      '¿Estás segura de eliminar esta nota?',
      '⚠️ Eliminar Nota',
      'Eliminar',
      'Cancelar'
    ).then((confirmed) => {
      if (confirmed) {
        this.examsService.deleteExam(gradeId).subscribe({
          next: () => {
            this.notificationService.showToast('Nota eliminada exitosamente', 'success')
            if (this.selectedStudent) {
              this.loadStudentGrades(this.selectedStudent.id)
            }
          },
          error: (err) => {
            console.error('Error deleting grade:', err)
            this.notificationService.showToast('Error al eliminar la nota', 'error')
          }
        })
      }
    })
  }

  cancelEdit() {
    this.newGrade = { type: 'EXAM', score: 0, subjectId: 0 }
    this.editingGradeId = null
  }

  getExamTypeLabel(type: string): string {
    const labels: any = {
      'EXAM': 'Parcial',
      'FINAL_EXAM': 'Final',
      'MAKEUP_EXAM': 'Recuperatorio'
    }
    return labels[type] || type
  }

  getSubjectName(subjectId?: number): string {
    if (!subjectId || !this.selectedCommission) return '-'
    const subject = this.selectedCommission.subjects.find(s => s.id === subjectId)
    return subject ? subject.name : '-'
  }
}

