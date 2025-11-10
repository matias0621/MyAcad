import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommissionService } from '../../../Services/Commission/commission-service';
import { AuthService } from '../../../Services/Auth/auth-service';
import Commission from '../../../Models/Commission/commission';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import Student from '../../../Models/Users/Student';
import { StudentService } from '../../../Services/Users/student-service';

interface ExamGrade {
  id?: number
  type: 'EXAM' | 'FINAL_EXAM' | 'MAKEUP_EXAM'
  score: number
  date?: string
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
  // Estado por comisión -> materia seleccionada y alumnos filtrados
  commissionSubjectsState: Record<number, { subjectId?: number, students: Student[], loading: boolean, error?: string }> = {}
  teacherId!: number

  // Modal de notas
  showGradesModal = false
  selectedStudent: Student | null = null
  selectedCommission: Commission | null = null
  studentGrades: ExamGrade[] = []
  newGrade: ExamGrade = { type: 'EXAM', score: 0 }
  editingGradeId: number | null = null
  gradesStore = new Map<number, ExamGrade[]>()

  constructor(
    private commissionService: CommissionService,
    private activatedRoute: ActivatedRoute,
    private authService: AuthService,
    private studentService: StudentService
  ) {
    this.programId = Number(this.activatedRoute.snapshot.params['programId'])
  }

  ngOnInit(): void {
    const token: any = this.authService.getDecodedToken()
    if (token) {
      this.teacherId = Number(token.id)
    }
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
        this.commissionList = (res as any[]).map((c: any) => this.normalizeCommission(c))
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
      if (!this.commissionSubjectsState[commissionId]) {
        this.commissionSubjectsState[commissionId] = { students: [], loading: false }
      }
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
    if (this.selectedStudent) {
      this.gradesStore.set(this.selectedStudent.id, [...this.studentGrades])
    }
    this.showGradesModal = false
    this.selectedStudent = null
    this.selectedCommission = null
    this.studentGrades = []
    this.newGrade = { type: 'EXAM', score: 0 }
    this.editingGradeId = null
  }

  loadStudentGrades(studentId: number) {
    this.studentGrades = this.gradesStore.get(studentId) || []
    
  }

  addGrade() {
    if (this.newGrade.score < 0 || this.newGrade.score > 10) {
      alert('La nota debe estar entre 0 y 10')
      return
    }

    if (this.editingGradeId) {
      // Editar nota existente
      const index = this.studentGrades.findIndex(g => g.id === this.editingGradeId)
      if (index !== -1) {
        this.studentGrades[index] = { ...this.newGrade, id: this.editingGradeId }
      }
      this.editingGradeId = null
    } else {
      // Agregar nueva nota
      const newId = this.studentGrades.length > 0 
        ? Math.max(...this.studentGrades.map(g => g.id || 0)) + 1 
        : 1
      this.studentGrades.push({ ...this.newGrade, id: newId, date: new Date().toISOString() })
    }

    
    if (this.selectedStudent) {
      this.gradesStore.set(this.selectedStudent.id, [...this.studentGrades])
    }
    this.newGrade = { type: 'EXAM', score: 0 }
  }

  editGrade(grade: ExamGrade) {
    this.newGrade = { ...grade }
    this.editingGradeId = grade.id || null
  }

  deleteGrade(gradeId: number) {
    if (confirm('¿Estás segura de eliminar esta nota?')) {
      this.studentGrades = this.studentGrades.filter(g => g.id !== gradeId)
      
      // Actualizar el store después de eliminar
      if (this.selectedStudent) {
        this.gradesStore.set(this.selectedStudent.id, [...this.studentGrades])
      }
    }
  }

  cancelEdit() {
    this.newGrade = { type: 'EXAM', score: 0 }
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

  // Seleccion de materia dentro de una comision
  onSubjectSelected(commission: Commission) {
    const state = this.commissionSubjectsState[commission.id]
    if (!state || !state.subjectId) {
      state && (state.students = [])
      return
    }
    this.fetchStudentsForCommissionSubject(commission.id, state.subjectId)
  }

  private fetchStudentsForCommissionSubject(commissionId: number, subjectId: number) {
    const state = this.commissionSubjectsState[commissionId]
    if (!state) return
    if (!this.teacherId) {
      state.error = 'No se pudo identificar el docente'
      return
    }
    state.loading = true
    state.error = undefined
    state.students = []
    this.studentService.getStudentsForTeacherSubjectCommission(this.teacherId, subjectId, commissionId).subscribe({
      next: (students) => {
        state.students = students
        state.loading = false
      },
      error: (err) => {
        console.error('Error cargando alumnos filtrados:', err)
        state.error = err.error?.message || 'Error al cargar alumnos'
        state.loading = false
      }
    })
  }

  // Helper seguro para el template
  getCommissionState(commissionId: number) {
    return this.commissionSubjectsState[commissionId] ?? { students: [], loading: false } as { subjectId?: number, students: Student[], loading: boolean, error?: string }
  }
}

