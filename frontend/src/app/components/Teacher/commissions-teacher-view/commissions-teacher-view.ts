import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommissionService } from '../../../Services/Commission/commission-service';
import { AuthService } from '../../../Services/Auth/auth-service';
import Commission from '../../../Models/Commission/commission';

@Component({
  selector: 'app-commissions-teacher-view',
  imports: [],
  templateUrl: './commissions-teacher-view.html',
  styleUrl: './commissions-teacher-view.css'
})
export class CommissionsTeacherView implements OnInit {
  programId!: number
  commissionList: Commission[] = []
  loading: boolean = true
  expandedCommissions = new Set<number>()

  constructor(
    private commissionService: CommissionService,
    private activatedRoute: ActivatedRoute,
    private authService: AuthService
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
}
