import { Component } from '@angular/core';
import { UserList } from "../../../components/user-list/user-list";

@Component({
  selector: 'app-students',
  imports: [UserList],
  templateUrl: './students.html',
  styleUrl: './students.css'
})
export class Students {
}
