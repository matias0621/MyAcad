import { Component, OnInit } from '@angular/core';
import Manager from '../../../Models/Users/Manager';
import { ManagerService } from '../../../Services/Users/manager-service';
import { UserForm } from "../../../components/user-form/user-form";

@Component({
  selector: 'app-managers',
  imports: [UserForm],
  templateUrl: './managers.html',
  styleUrl: './managers.css'
})
export class Managers implements OnInit {
  managers !: Manager[];

  constructor(
    private service: ManagerService
  ) { }

  ngOnInit(): void {
    this.getManagers();
  }

  getManagers() {
    this.service.getManagers().subscribe({
      next: (data) => { this.managers = data },
      error: (error) => { console.error(error) }
    })
  }

  deleteManager(id: number) {
    this.service.deleteManager(id).subscribe({
      next: (data) => { this.getManagers() },
      error: (error) => { console.error(error) }
    })
  }
}
