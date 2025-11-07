import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import Commission from '../../Models/Commission/commission';
import { RegistrationStudent } from '../../Models/Users/Student';


@Injectable({
  providedIn: 'root'
})
export class CommissionService {
  readonly API_URL = 'http://localhost:8080/commissions';
  constructor(
    private http: HttpClient
  ) { }

  getCommissions() {
    return this.http.get<Commission[]>(this.API_URL);
  }

  getByProgram(program: string) {
    return this.http.get<Commission[]>(`${this.API_URL}/program/${program}`);
  }

  postCommission(commission: Commission) {
    return this.http.post<Commission>(this.API_URL, commission);
  }

  putCommission(Commission: Commission) {
    return this.http.put<Commission>(`${this.API_URL}/${Commission.id}`, Commission);
  }

  addSubjectsToCommission(idCommission:number, idSubjects:number){
    return this.http.put(`${this.API_URL}/add-subject/${idCommission}`, idSubjects)
  }

  registerStudentToCommissionByManager(idCommision:number, requestStudent:RegistrationStudent){
    return this.http.put(`${this.API_URL}/register-student-by-manager/${idCommision}`,requestStudent)
  }

  deleteCommission(id: number) {
    return this.http.delete<Commission>(`${this.API_URL}/${id}`);
  }
}
