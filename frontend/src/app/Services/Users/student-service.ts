import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import Student from '../../Models/Users/Student';

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private readonly API_URL = 'http://localhost:8080/students';

  constructor(private http: HttpClient) {}

  getStudentsForTeacherSubjectCommission(teacherId: number, subjectId: number, commissionId: number) {
    return this.http.get<Student[]>(`${this.API_URL}/${teacherId}/subjects/${subjectId}/commissions/${commissionId}/students`);
  }
}
