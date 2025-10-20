import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  readonly API_URL = 'http://localhost:8080';

  constructor(
    private http: HttpClient
  ) { }

  getUsers(endpoint: string) {
    return this.http.get<any[]>(`${this.API_URL}/${endpoint}`);
  }

  postUser(user: any, endpoint: string) {
    return this.http.post<any>(`${this.API_URL}/${endpoint}`, user);
  }
}
