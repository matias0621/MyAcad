import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import Manager from '../../Models/Users/Manager';

@Injectable({
  providedIn: 'root'
})
export class ManagerService {
  readonly API_URL = 'http://localhost:8080/managers';

  constructor(
    private http: HttpClient
  ) { }

  getManagers() {
    return this.http.get<Manager[]>(this.API_URL);
  }

  getByLegajo(legajo: string){
    return this.http.get<Manager[]>(`${this.API_URL}/legajo/${legajo}`);
  }

  getByName(name: string){
    return this.http.get<Manager[]>(`${this.API_URL}/name/${name}`);
  }
  
  postManager(manager: Manager) {
    return this.http.post<Manager>(this.API_URL, manager);
  }

  deleteManager(id: number) {
      return this.http.delete<Manager>(`${this.API_URL}/${id}`);
    }
}
