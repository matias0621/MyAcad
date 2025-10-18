import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import Student from '../../Models/Users/Student';

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  readonly API_URL = 'http://localhost:8080/students';

  constructor(
    private http: HttpClient
  ) { }

  getStudents() {
    return this.http.get<Student[]>(this.API_URL);
  }

  postStudent(student: Student) {
    return this.http.post<Student>(this.API_URL, student);
  }

  deleteStudent(id: number) {
    return this.http.delete<Student>(`${this.API_URL}/${id}`);
  }
}
