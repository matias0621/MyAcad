import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../Services/Auth/auth-service';
import { DialogStateService } from '../../Services/ui/dialog-state.service';
import { MenuItem } from '../../Models/MenuItem/MenuItem';
import { MENU_CONFIG } from '../../menu.config';


@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class Header implements OnInit {
  token = localStorage.getItem('token');
  public isMenuOpen = false;


  menu = signal<MenuItem[]>([]);

  constructor(
    public service: AuthService,
    private dialogState: DialogStateService
  ) {}

  ngOnInit() {
    const role = this.service.getRole(); // Ej: "ROLE_MANAGER", "ROLE_STUDENT", "ROLE_TEACHER"

    if (role && MENU_CONFIG[role]) {
      this.menu.set(MENU_CONFIG[role]);
    } else {
      this.menu.set([]);
    }
  }

  logout() {
    this.service.logout();
  }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  closeMenu() {
    this.isMenuOpen = false;
  }

  openChangePassword() {
    this.closeMenu();
    setTimeout(() => {
      this.dialogState.openChangePassword();
    }, 0);
  }

  onDialogClosed() {
    this.dialogState.closeChangePassword();
  }
}
