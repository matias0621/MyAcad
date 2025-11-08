import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Notification, Modal, NotificationType, ModalType } from '../../Models/notification.model';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notificationId = 0;
  private toastsSubject = new BehaviorSubject<Notification[]>([]);
  private modalSubject = new BehaviorSubject<Modal | null>(null);
  private confirmResolve: ((value: boolean) => void) | null = null;

  public toasts$ = this.toastsSubject.asObservable();
  public modal$ = this.modalSubject.asObservable();

  // Toast auto-desaparecible 
  showToast(message: string, type: NotificationType = 'success', duration = 3000) {
    const notification: Notification = {
      id: ++this.notificationId,
      message,
      type,
      duration
    };

    const currentToasts = this.toastsSubject.value;
    this.toastsSubject.next([...currentToasts, notification]);

    setTimeout(() => {
      this.removeToast(notification.id);
    }, duration);
  }

  removeToast(id: number) {
    const currentToasts = this.toastsSubject.value;
    this.toastsSubject.next(currentToasts.filter(t => t.id !== id));
  }

  // Modal errores críticos y validaciones
  showAlert(message: string, type: ModalType = 'error') {
    this.modalSubject.next({ message, type });
  }

  closeAlert() {
    this.modalSubject.next(null);
  }

  // Modal de confirmación (reemplaza confirm())
  confirm(message: string, title = '¿Estás seguro?', confirmText = 'Sí', cancelText = 'No'): Promise<boolean> {
    return new Promise((resolve) => {
      this.confirmResolve = resolve;
      this.modalSubject.next({ 
        message, 
        type: 'confirm',
        title, 
        confirmText, 
        cancelText 
      });
    });
  }

  resolveConfirm(accepted: boolean) {
    if (this.confirmResolve) {
      this.confirmResolve(accepted);
      this.confirmResolve = null;
    }
    this.modalSubject.next(null);
  }

  // Métodos de conveniencia
  success(message: string) {
    this.showToast(message, 'success');
  }

  error(message: string, useModal = true) {
    if (useModal) {
      this.showAlert(message, 'error');
    } else {
      this.showToast(message, 'error', 4000);
    }
  }

  warning(message: string, useModal = false) {
    if (useModal) {
      this.showAlert(message, 'warning');
    } else {
      this.showToast(message, 'warning', 3500);
    }
  }

  info(message: string) {
    this.showToast(message, 'info');
  }
}
