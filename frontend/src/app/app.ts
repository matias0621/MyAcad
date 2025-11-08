import { Component, signal } from '@angular/core';
import { Router, RouterOutlet, NavigationEnd } from '@angular/router';
import { Header } from './components/header/header';
import { Footer } from "./components/footer/footer";
import { CommonModule } from '@angular/common';
import { filter } from 'rxjs/operators';
import { NotificationContainerComponent } from './components/notification-container/notification-container';
import { AlertDialogComponent } from './components/alert-dialog/alert-dialog';
import { ChangePasswordDialogComponent } from './components/change-password-dialog/change-password-dialog';
import { DialogStateService } from './Services/ui/dialog-state.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, Footer, CommonModule, NotificationContainerComponent, AlertDialogComponent, ChangePasswordDialogComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
  showHeader = true;
  showFooter = true;

  constructor(private router: Router, public dialogState: DialogStateService) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      const isLoginPage = event.url === '/auth/login' || event.url.startsWith('/auth/login');
      this.showHeader = !isLoginPage;
      this.showFooter = !isLoginPage;
    });
  }
}
