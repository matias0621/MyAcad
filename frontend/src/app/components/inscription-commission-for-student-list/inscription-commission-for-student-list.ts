import { ChangeDetectorRef, Component } from '@angular/core';
import { Router } from '@angular/router';
import { InscriptionToCommission } from '../../Models/InscriptionToCommission/InscritionToCommission';
import { InscriptionToCommissionService } from '../../Services/InscriptionToCommission/inscription-to-commission-service';
import { DatePipe } from '@angular/common';
import { InscriptionCommissionForStudentForm } from '../inscription-commission-for-student-form/inscription-commission-for-student-form';
import { InscriptionCommissionForStudentFormEdit } from '../inscription-commission-for-student-form-edit/inscription-commission-for-student-form-edit';
import { NotificationService } from '../../Services/notification/notification.service';

declare const bootstrap: { Modal: any };

@Component({
  selector: 'app-inscription-commission-for-student-list',
  imports: [DatePipe, InscriptionCommissionForStudentForm, InscriptionCommissionForStudentFormEdit],
  templateUrl: './inscription-commission-for-student-list.html',
  styleUrl: './inscription-commission-for-student-list.css'
})


export class InscriptionCommissionForStudentList {

  inscriptionList !: InscriptionToCommission[];
  selectedInscription?: InscriptionToCommission;
  editingInscription: InscriptionToCommission | null = null;
  private pendingSelectionId: number | null = null;

  constructor(public inscriptionService: InscriptionToCommissionService,
    private crd: ChangeDetectorRef,
    private notificationService: NotificationService,
    private router: Router) { }

  ngOnInit(): void {
    this.getAllInscription()
  }

  getAllInscription() {
    this.inscriptionService.getAllInscriptions().subscribe({
      next: (res) => {
        this.inscriptionList = [...res];
        if (this.pendingSelectionId !== null) {
          this.selectedInscription = this.inscriptionList.find(item => item.id === this.pendingSelectionId) ?? this.selectedInscription;
          this.pendingSelectionId = null;
        }
        this.crd.detectChanges();
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  deleteCommission(id: number) {
    this.notificationService.confirm(
      '¿Estás seguro de que deseas eliminar esta comisión?',
      'Confirmar eliminación',
      'Eliminar',
      'Cancelar'
    ).then((confirmed) => {
      if (confirmed) {
        this.inscriptionService.deleteInscription(id).subscribe({
          next: (res) => {
            this.getAllInscription()
          },
          error: (err) => {
            console.log(err)
          }
        })
      }
    });
  }

  openEdit(inscription: InscriptionToCommission) {
    this.editingInscription = inscription;
  }

  handleInscriptionCreated() {
    this.closeModal('modal-add');
    setTimeout(() => {
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(['/inscriptionToCommission']);
      });
    }, 100);
  }

  handleInscriptionUpdated() {
    const updatedId = this.editingInscription?.id ?? null;
    this.pendingSelectionId = updatedId;
    this.getAllInscription();
    this.closeModal('modal-edit');
    this.editingInscription = null;
  }

  private closeModal(modalId: string) {
    const element = document.getElementById(modalId);
    if (!element || typeof bootstrap === 'undefined') {
      return;
    }

    const instance = bootstrap.Modal.getInstance(element) ?? new bootstrap.Modal(element);
    instance.hide();
  }

}
