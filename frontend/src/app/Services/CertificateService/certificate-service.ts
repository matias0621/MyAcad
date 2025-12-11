import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  private api_URL = "http://localhost:8080/certificate";

  constructor(private http: HttpClient) { }

  downloadRegularStudentCertificate(studentId: number) {
    return this.http.get(`${this.api_URL}/regular-student/${studentId}`, {
      responseType: 'blob'
    });
  }

  downloadAcademicActivityCertificate(studentId: number) {
    return this.http.get(`${this.api_URL}/academic-activity/${studentId}`, {
      responseType: 'blob'
    });
  }
}
