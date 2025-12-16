import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import Teacher from '../../Models/Users/Teachers';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  readonly API_URL = 'http://localhost:8080';
  constructor(private http: HttpClient) {}

  getUsers(endpoint: string) {
    return this.http.get<any>(`${this.API_URL}/${endpoint}`);
  }

  getUsersByCommission(commissionId: number, endpoint: string) {
    return this.http.get<any>(`${this.API_URL}/${endpoint}/commission/${commissionId}`);
  }

  getTeacherByCommissionAndSubject(commissionId:number, subjectId:number){
    return this.http.get<Teacher>(`${this.API_URL}/teachers/commission/${commissionId}/subject/${subjectId}`)
  }

  getUsersPaginated(endpoint: string, page: number, size: number) {
    return this.http.get<any>(`${this.API_URL}/${endpoint}/paginated?page=${page}&size=${size}`);
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

  uploadCsv(formData: FormData, endpoint: string) {
    return this.http.post(`${this.API_URL}/${endpoint}/upload-by-csv`, formData);
  }

  deleteUser(id: number, endpoint: string) {
    return this.http.delete<any>(`${this.API_URL}/${endpoint}/${id}`);
  }

  definitiveDeleteUser(id: number, endpoint: string) {
    return this.http.delete<any>(`${this.API_URL}/${endpoint}/delete/${id}`);
  }
}
