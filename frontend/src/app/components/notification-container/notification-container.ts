import { Component, OnInit } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { NotificationService } from '../../Services/notification/notification.service';

@Component({
  selector: 'app-notification-container',
  standalone: true,
  imports: [AsyncPipe],
  templateUrl: './notification-container.html',
  styleUrl: './notification-container.css'
})
export class NotificationContainerComponent implements OnInit {
  notifications$;

  constructor(private notificationService: NotificationService) {
    this.notifications$ = this.notificationService.toasts$;
  }

  ngOnInit() {
    
  }

  close(id: number) {
    this.notificationService.removeToast(id);
  }
}
