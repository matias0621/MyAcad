import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Technicals } from '../../pages/careers/technicals/technicals';

@Injectable({
  providedIn: 'root'
})
export class TechnicalService {
  readonly API_URL = 'http://localhost:8080/technicals';

  constructor(
    private http: HttpClient
  ) { }

  getTechnicals() {
    return this.http.get<Technicals[]>(this.API_URL);
  }

  postTechnical(technical: Technicals) {
    return this.http.post<Technicals>(this.API_URL, technical);
  }

  deleteTechnical(id: number) {
    return this.http.delete<Technicals>(`${this.API_URL}/${id}`);
  }

}
