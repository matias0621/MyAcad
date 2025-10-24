import { Component } from '@angular/core';
import { UserForm } from "../../../components/user-form/user-form";
import { FormsModule } from '@angular/forms';
import { UserList } from "../../../components/user-list/user-list";

@Component({
  selector: 'app-teachers',
  imports: [UserForm, FormsModule, UserList],
  templateUrl: './teachers.html',
  styleUrl: './teachers.css'
})
export class Teachers {

}
