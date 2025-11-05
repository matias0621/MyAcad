import { Component } from '@angular/core';
import { UserList } from "../../../components/Users/user-list/user-list";

@Component({
  selector: 'app-managers',
  imports: [UserList],
  templateUrl: './managers.html',
  styleUrl: './managers.css'
})
export class Managers {
  
}
