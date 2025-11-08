import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  readonly API_URL = 'http://localhost:8080';
  constructor(
    private http: HttpClient
  ) {}

  getUsers(endpoint: string) {
    return this.http.get<any[]>(`${this.API_URL}/${endpoint}`);
  }

  getByLegajo(legajo: string, endpoint: string) {
    return this.http.get<any[]>(`${this.API_URL}/${endpoint}/legajo/${legajo}`);
  }

  getByName(name: string, endpoint: string) {
    return this.http.get<any[]>(`${this.API_URL}/${endpoint}/name/${name}`);
  }

  postUser(user: any, endpoint: string) {
    return this.http.post<any>(`${this.API_URL}/${endpoint}`, user);
  }

  putUser(user: any, endpoint: string) {
    return this.http.put<any>(`${this.API_URL}/${endpoint}`, user);
  }

  deleteUser(id: number, endpoint: string) {
    return this.http.delete<any>(`${this.API_URL}/${endpoint}/${id}`);
  }

  definitiveDeleteUser(id: number, endpoint: string) {
    return this.http.delete<any>(`${this.API_URL}/${endpoint}/delete/${id}`);
  }
}
