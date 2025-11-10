import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Exams, ExamsPost } from '../../Models/Exam/Exam';


@Injectable({
  providedIn: 'root'
})
export class ExamsService {
  readonly url_api = "http://localhost:8080/exams"


  constructor(private http:HttpClient){}

  getAllExams() {
    return this.http.get<Exams[]>(`${this.url_api}`);
  }

  getExamById(id: number) {
    return this.http.get<Exams>(`${this.url_api}/${id}`);
  }

  getExamsBySubjects(subjectsId: number) {
    return this.http.get<Exams[]>(`${this.url_api}/subjects/${subjectsId}`);
  }

  getExamsByStudents(studentId:number){
    return this.http.get<Exams[]>(`${this.url_api}/student/${studentId}`)
  }

  getExamsByTeacher(teacherId: number) {
    return this.http.get<Exams[]>(`${this.url_api}/teacher/${teacherId}`)
  }

  updateExamScore(examId: number, score: number) {
    return this.http.put(`${this.url_api}/${examId}/score`, { score })
  }

  postExam(exam: ExamsPost) {
    return this.http.post(`${this.url_api}`, exam);
  }

  putExam(id: number, exam: ExamsPost) {
    return this.http.put(`${this.url_api}/${id}`, exam);
  }

  deleteExam(examId: number) {
    return this.http.delete(`${this.url_api}/${examId}`);
  }

}
