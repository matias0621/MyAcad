import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import Program from '../Models/Program/Program';

@Injectable({
  providedIn: 'root'
})
export class ProgramService {
  readonly API_URL = 'http://localhost:8080/programs';
  constructor(
    private http: HttpClient
  ) { }

  getPrograms() {
    return this.http.get<Program[]>(this.API_URL);
  }

  getProgramsByStudent(studentId: number) {
    return this.http.get<Program[]>(`${this.API_URL}/student/${studentId} `);
  }

  getProgramsByTeacher(teacherId: number) {
    return this.http.get<Program[]>(`${this.API_URL}/teacher/${teacherId} `);
  }
}
