import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Exams, ExamsPost } from '../../Models/Exam/Exam';


@Injectable({
  providedIn: 'root'
})
export class ExamsService {
  readonly url_api = "http://localhost:8080/exams"


  constructor(private http: HttpClient) { }

  getAllExams() {
    return this.http.get<Exams[]>(`${this.url_api}`);
  }

  getExamsPaginated(page: number, size: number) {
    return this.http.get<any>(`${this.url_api}/paginated?page=${page}&size=${size}`);
  }

  getExamById(id: number) {
    return this.http.get<Exams>(`${this.url_api}/${id}`);
  }

  getExamsBySubjects(subjectsId: number) {
    return this.http.get<Exams[]>(`${this.url_api}/subjects/${subjectsId}`);
  }

  getExamsByStudents(studentId: number) {
    return this.http.get<Exams[]>(`${this.url_api}/student/${studentId}`)
  }

  getExamsByStudentIdAndProgram(studentId: number, programName: String) {
    console.log(`${this.url_api}/student/${studentId}/program/${programName}`)
    return this.http.get<Exams[]>(`${this.url_api}/student/${studentId}/program/${programName}`)
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
