import { Component } from '@angular/core';
import { InscriptionToCommission } from '../../../Models/InscriptionToCommission/InscritionToCommission';
import { InscriptionToCommissionService } from '../../../Services/InscriptionToCommission/inscription-to-commission-service';
import { CommissionService } from '../../../Services/Commission/commission-service';
import { AuthService } from '../../../Services/Auth/auth-service';
import { NotificationService } from '../../../Services/notification/notification.service';
import { SubjectsXStudentService } from '../../../Services/SubjectsXStudent/subjects-xstudent-service';

import Subjects from '../../../Models/Subjects/Subjects';
import { SubjectsXStudent } from '../../../Models/SubjectsXStudent/SubjectsXStudent';
import Commission from '../../../Models/Commission/commission';
import Student from '../../../Models/Users/Student';

@Component({
  selector: 'app-register-commission',
  templateUrl: './register-commission.html',
  styleUrl: './register-commission.css',
})
export class RegisterCommission {

  inscriptionList: InscriptionToCommission[] = [];
  listOfSubjectsXStudent: SubjectsXStudent[] = [];
  token!: any;

  constructor(
    private inscriptionService: InscriptionToCommissionService,
    private commissionService: CommissionService,
    private authService: AuthService,
    private notificationService: NotificationService,
    private subjectXStudentService: SubjectsXStudentService
  ) {}

  ngOnInit(): void {
    this.token = this.authService.getDecodedToken();
    this.loadData();
  }

  /* ===========================
     📦 CARGA DE DATOS
     =========================== */

  private loadData(): void {
    this.inscriptionService
      .getInscriptionByStudent(this.token.id)
      .subscribe(res => {
        this.inscriptionList = res;
      });

    this.subjectXStudentService
      .getSubjectsByStudentId(this.token.id)
      .subscribe(res => {
        this.listOfSubjectsXStudent = res;
      });
  }

  /* ===========================
     🔍 CHEQUEOS CORRECTOS
     =========================== */

  // Está en ESTA materia Y ESTA comisión
  isStudentInSubjectAndCommission(
    subject: Subjects,
    commission: Commission
  ): boolean {
    return this.listOfSubjectsXStudent.some(
      s =>
        s.subjects.id === subject.id &&
        s.commission.id === commission.id
    );
  }

  // Está en la materia pero en OTRA comisión
  isStudentInSubjectOtherCommission(
    subject: Subjects,
    commission: Commission
  ): boolean {
    return this.listOfSubjectsXStudent.some(
      s =>
        s.subjects.id === subject.id &&
        s.commission.id !== commission.id
    );
  }

  // Está en la comisión (sin importar la materia)
  isStudentInCommission(commission: Commission): boolean {
    return commission.students.some(
      (s: Student) => s.id === this.token.id
    );
  }

  /* ===========================
     🎯 ACCIONES
     =========================== */

  registerByStudent(subjectId: number, commissionId: number): void {
    this.commissionService
      .regiterByStudent(commissionId, subjectId)
      .subscribe({
        next: () => {
          this.notificationService.success('Te registraste correctamente');
          this.loadData();
        },
        error: err =>
          this.notificationService.error(err.error, true),
      });
  }

  unregister(subjectId: number, commissionId: number): void {
    this.commissionService.unregisterByStudent(this.token.id, subjectId,commissionId).subscribe({
      next: () => {
        this.notificationService.success("Te diste de baja de la materia")
        this.loadData()
      },
      error: (err) => {
        console.log(err)
        this.notificationService.error("Hubo un error intente denuevo mas tarde", true)
      }
    })
  }
}
