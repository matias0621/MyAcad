import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import Teacher from '../../Models/Users/Teachers';

@Injectable({
  providedIn: 'root'
})
export class TeacherService {
  readonly API_URL = 'http://localhost:8080/teachers';

  constructor(
    private http: HttpClient
  ) { }

  getTeachers() {
    return this.http.get<Teacher[]>(this.API_URL);
  }

  getByLegajo(legajo: string){
    return this.http.get<Teacher[]>(`${this.API_URL}/legajo/${legajo}`);
  }

  getByName(name: string){
    return this.http.get<Teacher[]>(`${this.API_URL}/name/${name}`);
  }
  
  postTeacher(teacher: Teacher) {
    return this.http.post<Teacher>(this.API_URL, teacher);
  }
  deleteTeacher(id: number) {
    return this.http.delete<Teacher>(`${this.API_URL}/${id}`);
  }
}
