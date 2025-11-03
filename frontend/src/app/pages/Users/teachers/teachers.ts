import { Component } from '@angular/core';
import { UserList } from "../../../components/user-list/user-list";

@Component({
  selector: 'app-teachers',
  imports: [UserList],
  templateUrl: './teachers.html',
  styleUrl: './teachers.css'
})
export class Teachers {

}
