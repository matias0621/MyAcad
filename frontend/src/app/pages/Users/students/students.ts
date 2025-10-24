import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UserForm } from "../../../components/user-form/user-form";
import { UserList } from "../../../components/user-list/user-list";

@Component({
  selector: 'app-students',
  imports: [UserForm, FormsModule, UserList],
  templateUrl: './students.html',
  styleUrl: './students.css'
})
export class Students {
}
