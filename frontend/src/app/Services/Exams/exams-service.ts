import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Exam, PostExam } from '../../Models/Exam/Exam';
import { ExamFinal, PostExamFinal } from '../../Models/Final-Exam/FinalExam';

@Injectable({
  providedIn: 'root'
})
export class ExamsService {
  readonly url_api = "http://localhost:8080/"


  constructor(private http:HttpClient){}

  getAllExams(endpoint:string){
    return this.http.get<Exam[] | ExamFinal[]>(`${this.url_api}/${endpoint}`)
  }

  getExamById(endpoint:string, id:number){
    return this.http.get<Exam | ExamFinal>(`${this.url_api}/${endpoint}/${id}`)
  }

  getExamsBySubjects(endpoint:string, subjectsId:number){
    return this.http.get<Exam[] | ExamFinal[]>(`${this.url_api}/${endpoint}/subjects/${subjectsId}`)
  }

  postExam(endpoint:string, exam:PostExam | PostExamFinal){
    return this.http.post(`${this.url_api}/${endpoint}`, exam)
  }

  putExam(endpoint:string, exam:Exam | ExamFinal){
    return this.http.put(`${this.url_api}/${endpoint}/${exam.id}`, exam)
  }

  deleteExam(endpoint:string, examId:number){
    return this.http.delete(`${this.url_api}/${endpoint}/${examId}`)
  }

}
