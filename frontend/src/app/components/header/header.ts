import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../Services/Auth/auth-service';
import { DialogStateService } from '../../Services/ui/dialog-state.service';

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

  constructor(
    public service : AuthService,
    private dialogState: DialogStateService
  ){}

  ngOnInit(){
  }
  logout(){
    this.service.logout();
  }

  toggleMenu(){
    this.isMenuOpen = !this.isMenuOpen;
  }

  closeMenu(){
    this.isMenuOpen = false;
  }

  openChangePassword(){
    this.closeMenu();
    setTimeout(() => {
      this.dialogState.openChangePassword();
    }, 0);
  }

  onDialogClosed(){
    this.dialogState.closeChangePassword();
  }
}