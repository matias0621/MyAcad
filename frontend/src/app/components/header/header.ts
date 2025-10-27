import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../Services/Auth/auth-service';

@Component({
  selector: 'app-header',
  imports: [RouterLink],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class Header {

  constructor(
    private service : AuthService
  ){}
  logout(){
    this.service.logout();
  }
}
