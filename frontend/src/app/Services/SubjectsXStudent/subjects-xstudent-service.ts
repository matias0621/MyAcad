import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SubjectsXStudent } from '../../Models/SubjectsXStudent/SubjectsXStudent';

@Injectable({
  providedIn: 'root'
})
export class SubjectsXStudentService {
  readonly api_url = 'http://localhost:8080/subject-x-student';

  constructor(private http : HttpClient){}

  getAllSubjectsXStudent(){
    return this.http.get<SubjectsXStudent[]>(`${this.api_url}`);
  }

  getSubjectsByStudentId(id: number){
    return this.http.get<SubjectsXStudent[]>(`${this.api_url}/student/${id}`);
  }

  getSubjectsByStudentIdAnsSubjectsId(studentId:number, subjectsId:number){
    return this.http.get<SubjectsXStudent>(`${this.api_url}/student/${studentId}/subject/${subjectsId}`)
  }
  
  getBySubjectIdStudentIdCommissionId(subjectId:number, studentId:number, commissionId:number){
    return this.http.get<SubjectsXStudent>(`${this.api_url}/student/${studentId}/commission/${commissionId}/subject/${subjectId}`)
  }
}
