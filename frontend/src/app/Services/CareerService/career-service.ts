import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import Career from '../../Models/Users/Careers/Career';
import Course from '../../Models/Users/Careers/Course';
import Technical from '../../Models/Users/Careers/Technical';

@Injectable({
  providedIn: 'root'
})
export class CareerService {
  readonly API_URL = 'http://localhost:8080';

  private careerSelected: string | null = null

  constructor(
    private http: HttpClient
  ) { }

  getCareersPaginated(endpoint: string, page: number, size: number) {
    return this.http.get<any>(`${this.API_URL}/${endpoint}/paginated?page=${page}&size=${size}`);
  }

  getCareers(endpoint: string) {
    return this.http.get<Course[] | Technical[] | Career[]>(`${this.API_URL}/${endpoint}`);
  }

  postCareer(career: any, endpoint: string) {
    return this.http.post<Course | Technical | Career>(`${this.API_URL}/${endpoint}`, career);
  }

  deleteCareer(id: number, endpoint: string) {
    return this.http.delete<Course | Technical | Career>(`${this.API_URL}/${endpoint}/${id}`);
  }

  definitiveDeleteCareer(id: number, endpoint: string) {
    return this.http.delete<Course | Technical | Career>(`${this.API_URL}/delete/${endpoint}/${id}`);
  }

  updateCareer(career: Career, endpoint: string) {
    return this.http.put<Course | Technical | Career>(`${this.API_URL}/${endpoint}/${career.id}`, career);
  }

  getCareerSelected(): string | null {
    return this.careerSelected
  }

  setCareerSelected(name: string | null) {
    this.careerSelected = name
  }

}
