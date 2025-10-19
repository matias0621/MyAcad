import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import Career from '../../Models/Users/Careers/Career';

@Injectable({
  providedIn: 'root'
})
export class CareerService {
  readonly API_URL = 'http://localhost:8080/careers';

  constructor(
    private http: HttpClient
  ) { }

  getCareers() {
    return this.http.get<Career[]>(this.API_URL);
  }

  postCareer(career: Career) {
    return this.http.post<Career>(this.API_URL, career);
  }

  deleteCareer(id: string) {
    return this.http.delete<Career>(`${this.API_URL}/${id}`);
  }

  updateCareer(career: Career) {
    return this.http.put<Career>(`${this.API_URL}/${career.id}`, career);
  }

}
