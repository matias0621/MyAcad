import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import Commission from '../../Models/Commission/commission';
import { RegistrationStudentOrTeacher } from '../../Models/Users/Student';


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

  getCommissionsPaginated(page: number, size: number) {
    return this.http.get<any>(`${this.API_URL}/paginated?page=${page}&size=${size}`);
  }

  getCommissionByStudentInfo(programName: string) {
    return this.http.get<Commission[]>(`${this.API_URL}/program/info-student/${programName}`)
  }



  getCommissionsByTeacher(teacherId: number) {
    return this.http.get<Commission[]>(`${this.API_URL}/teacher/${teacherId}`)
  }

  getCommissionsByProgramAndTeacher(programId: number, teacherId: number) {

    return this.http.get<Commission[]>(`${this.API_URL}/teacher/${teacherId}?programId=${programId}`)
  }

  getCommissionNotEnrolled(program:string){
    return this.http.get<Commission[]>(`${this.API_URL}/program/not-enrolled/${program}`)
  }

  getByProgram(program: string) {
    return this.http.get<Commission[]>(`${this.API_URL}/program/${program}`);
  }

  getById(id: number) {
    return this.http.get<Commission>(`${this.API_URL}/${id}`)
  }

  postCommission(commission: Commission) {
    return this.http.post<Commission>(this.API_URL, commission);
  }

  putCommission(Commission: Commission) {
    return this.http.put<Commission>(`${this.API_URL}/${Commission.id}`, Commission);
  }

  addSubjectsToCommission(idCommission: number, idSubjects: number) {
    return this.http.put(`${this.API_URL}/add-subject/${idCommission}`, idSubjects)
  }

  registerStudentToCommissionByManager(idCommision: number, requestStudent: RegistrationStudentOrTeacher) {
    return this.http.put(`${this.API_URL}/register-student-by-manager/${idCommision}`, requestStudent)
  }
  registerTeacherToCommissionByManager(idCommision: number, requestTeacher: RegistrationStudentOrTeacher) {
    return this.http.put(`${this.API_URL}/register-teacher-by-manager/${idCommision}`, requestTeacher)
  }
  regiterByStudent(commissionId: number, subjectsId: number) {
    return this.http.put(`${this.API_URL}/register-commision-to-student/${commissionId}`, subjectsId)
  }

  definitiveDeleteCommission(id: number) {
    return this.http.delete<Commission>(`${this.API_URL}/delete/${id}`);
  }

  deleteCommission(id: number) {
    return this.http.delete<Commission>(`${this.API_URL}/${id}`);
  }
}
