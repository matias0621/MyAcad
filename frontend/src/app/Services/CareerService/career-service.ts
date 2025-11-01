import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import Career from '../../Models/Users/Careers/Career';

@Injectable({
  providedIn: 'root'
})
export class CareerService {
  readonly API_URL = 'http://localhost:8080';

  constructor(
    private http: HttpClient
  ) { }

  getCareers(endpoint: string) {
    return this.http.get<any[]>(`${this.API_URL}/${endpoint}`);
  }

  postCareer(career: any, endpoint: string) {
    return this.http.post<any>(`${this.API_URL}/${endpoint}`, career);
  }

  deleteCareer(id: number, endpoint: string) {
    return this.http.delete<any>(`${this.API_URL}/${endpoint}/${id}`);
  }

  updateCareer(career: Career, endpoint: string) {
    return this.http.put<any>(`${this.API_URL}/${endpoint}/${career.id}`, career);
  }

}
