import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { InscriptionToFinalExamService } from '../../Services/InscriptionToFinalExam/inscription-to-final-exam-service';
import { InscriptionToFinalExam } from '../../Models/InscriptionToFinalExam/InscriptionToFinalExam';
import { InscriptionToExamForm } from "../inscription-to-exam-form/inscription-to-exam-form";
import { InscriptionToExamFormEdit } from "../inscription-to-exam-form-edit/inscription-to-exam-form-edit";
import { DatePipe } from '@angular/common';
import { NotificationService } from '../../Services/notification/notification.service';

declare const bootstrap: { Modal: any };

@Component({
  selector: 'app-inscription-to-exam-list',
  imports: [InscriptionToExamForm, InscriptionToExamFormEdit, DatePipe],
  templateUrl: './inscription-to-exam-list.html',
  styleUrl: './inscription-to-exam-list.css'
})
export class InscriptionToExamList implements OnInit {
  inscriptionList !: InscriptionToFinalExam[];
  selectedInscription ?: InscriptionToFinalExam;
  editingInscription: InscriptionToFinalExam | null = null;
  private pendingSelectionId: number | null = null;

  constructor(public inscriptionService:InscriptionToFinalExamService,
    private crd:ChangeDetectorRef,
    private notificationService: NotificationService){}

  ngOnInit(): void {
    this.getAllInscription()
  }
  
  getAllInscription(){
    this.inscriptionService.getAllInscription().subscribe({
      next: (res) => {
        this.inscriptionList = res;
        console.log(res)
        if (this.pendingSelectionId !== null) {
          this.selectedInscription = this.inscriptionList.find(item => item.id === this.pendingSelectionId) ?? this.selectedInscription;
          this.pendingSelectionId = null;
        }
        this.crd.detectChanges()
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  deleteExam(id: number) {
    this.notificationService.confirm(
      '¿Estás seguro de que deseas eliminar este examen?',
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


  openEdit(inscription: InscriptionToFinalExam) {
    this.editingInscription = inscription;
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
