import { Component, OnInit } from '@angular/core';
import { UserForm } from "../../../components/user-form/user-form";
import { FormsModule } from '@angular/forms';
import { UserList } from "../../../components/user-list/user-list";

@Component({
  selector: 'app-managers',
  imports: [UserForm, FormsModule, UserList],
  templateUrl: './managers.html',
  styleUrl: './managers.css'
})
export class Managers {
  
}
