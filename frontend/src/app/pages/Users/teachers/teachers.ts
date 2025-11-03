import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UserList } from "../../../components/Users/user-list/user-list";
import { UserEditForm } from '../../../components/Users/user-edit-form/user-edit-form';
import { UserForm } from '../../../components/Users/user-form/user-form';

@Component({
  selector: 'app-teachers',
  imports: [UserList],
  templateUrl: './teachers.html',
  styleUrl: './teachers.css'
})
export class Teachers {

}
