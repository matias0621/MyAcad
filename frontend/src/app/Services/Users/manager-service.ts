import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import Manager from '../../Models/Users/Manager';

@Injectable({
  providedIn: 'root'
})
export class ManagerService {
  readonly API_URL = 'http://localhost:8080/teachers';

  constructor(
    private http: HttpClient
  ) { }

  getStudents() {
    return this.http.get<Manager[]>(this.API_URL);
  }

  postStudent(manager: Manager) {
    return this.http.post<Manager>(this.API_URL, manager);
  }
}
