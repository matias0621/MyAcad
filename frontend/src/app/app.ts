import { Component, signal } from '@angular/core';
import { Router, RouterOutlet, NavigationEnd } from '@angular/router';
import { Header } from './components/header/header';
import { Footer } from "./components/footer/footer";
import { CommonModule } from '@angular/common';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, Footer, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
  showHeader = true;
  showFooter = true;

  constructor(private router: Router) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      const isLoginPage = event.url === '/auth/login' || event.url.startsWith('/auth/login');
      this.showHeader = !isLoginPage;
      this.showFooter = !isLoginPage;
    });
  }
}
