import { Component, OnInit } from '@angular/core';
import { NotificationService } from '../../Services/notification/notification.service';
import { Modal } from '../../Models/notification.model';

@Component({
  selector: 'app-alert-dialog',
  standalone: true,
  imports: [],
  templateUrl: './alert-dialog.html',
  styleUrl: './alert-dialog.css'
})
export class AlertDialogComponent implements OnInit {
  alert: Modal | null = null;

  constructor(private notificationService: NotificationService) {}

  ngOnInit() {
    this.notificationService.modal$.subscribe(alert => {
      this.alert = alert;
    });
  }

  close() {
    this.notificationService.closeAlert();
  }

  accept() {
    this.notificationService.resolveConfirm(true);
  }

  cancel() {
    this.notificationService.resolveConfirm(false);
  }
}
