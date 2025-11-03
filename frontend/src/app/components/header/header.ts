import {
  Component, ElementRef, QueryList, ViewChild, ViewChildren,
  AfterViewInit, OnInit, HostListener, inject
} from '@angular/core';
import { Router, NavigationEnd, RouterLink } from '@angular/router';
import { AuthService } from '../../Services/Auth/auth-service';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-header',
  imports: [RouterLink],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class Header implements OnInit, AfterViewInit {
  token = localStorage.getItem('token');
  isMenuOpen = false;

  @ViewChild('indicator', { static: false }) indicatorRef!: ElementRef<HTMLSpanElement>;
  @ViewChild('blob', { static: false }) blobRef!: ElementRef<HTMLSpanElement>;
  @ViewChild('menuHost', { static: false }) menuHostRef!: ElementRef<HTMLElement>;
  @ViewChildren('menuItem') menuItems!: QueryList<ElementRef<HTMLElement>>;

  activeSection: 'inicio' | 'usuarios' | 'academico' | 'ofertas' = 'inicio';

  private router = inject(Router);

  constructor(public service: AuthService) {}

  ngOnInit(): void {
    this.activeSection = this.sectionFromUrl(this.router.url);
    this.router.events
      .pipe(filter(e => e instanceof NavigationEnd))
      .subscribe((e: any) => {
        this.activeSection = this.sectionFromUrl(e.urlAfterRedirects || e.url);
        queueMicrotask(() => this.restoreActiveIndicator());
      });
  }

  ngAfterViewInit(): void {
    setTimeout(() => this.restoreActiveIndicator());
  }

  @HostListener('window:resize')
  onResize() {
    this.restoreActiveIndicator();
  }

  private sectionFromUrl(url: string): 'inicio' | 'usuarios' | 'academico' | 'ofertas' {
    if (!url || url === '/' || url.startsWith('/#') || url === '') return 'inicio';
    if (url.startsWith('/students') || url.startsWith('/teachers') || url.startsWith('/managers')) return 'usuarios';
    if (url.startsWith('/subject') || url.startsWith('/exam') || url.startsWith('/final-exam')) return 'academico';
    if (url.startsWith('/engineerings') || url.startsWith('/technicals') || url.startsWith('/courses')) return 'ofertas';
    return 'inicio';
  }

  hoverTo(targetEl: HTMLElement) {
    this.moveIndicator(targetEl);
    this.moveBlob(targetEl, false);
  }

  activateAndSnap(targetEl: HTMLElement, section: typeof this.activeSection) {
    this.activeSection = section;
    this.moveIndicator(targetEl);
    this.moveBlob(targetEl, true);
  }

  restoreActiveIndicator() {
    const activeElRef = this.menuItems.find(ref => ref.nativeElement.classList.contains('active'));
    const target = activeElRef?.nativeElement || this.menuItems.first?.nativeElement;
    if (!target) return;
    this.moveIndicator(target);
    this.moveBlob(target, true);
  }

  private moveIndicator(target: HTMLElement) {
    const indicator = this.indicatorRef?.nativeElement;
    if (!indicator || !target) return;
    indicator.style.width = `${target.offsetWidth}px`;
    indicator.style.transform = `translateX(${target.offsetLeft}px)`;
  }

  private moveBlob(target: HTMLElement, snap: boolean) {
    const blob = this.blobRef?.nativeElement;
    const host = this.menuHostRef?.nativeElement;
    if (!blob || !host || !target) return;
    const centerX = target.offsetLeft + target.offsetWidth / 2;
    host.style.setProperty('--blob-x', `${centerX}px`);
    blob.classList.remove('snap');
    void blob.offsetWidth; // reinicia animación
    if (snap) blob.classList.add('snap');
  }

  logout() { this.service.logout(); }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  @HostListener('document:click', ['$event'])
  closeMenuOnOutsideClick(event: MouseEvent) {
    const target = event.target as HTMLElement;
    const isClickInsideNav = target.closest('nav') || target.closest('.hamburger-btn');
    if (!isClickInsideNav && this.isMenuOpen) {
      this.isMenuOpen = false;
    }
  }
}
