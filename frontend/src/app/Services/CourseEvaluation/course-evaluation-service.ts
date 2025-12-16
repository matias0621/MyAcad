import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CourseEvaluation, PostCourseEvaluation } from '../../Models/CourseEvaluation/CourseEvaluation';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CourseEvaluationService {
  readonly api_url = "http://localhost:8080/course-evaluations"

  constructor(private http: HttpClient) {}


  create(dto: PostCourseEvaluation): Observable<void> {
    return this.http.post<void>(this.api_url, dto);
  }

  getAllEvaluation(page:number, size:number){
    return this.http.get<CourseEvaluation>(`${this.api_url}/paginated?page=${page}&size=${size}`)
  }

  getBySubject(subjectId:number, page:number, size:number){
    return this.http.get<CourseEvaluation>(`${this.api_url}/paginated/subject/${subjectId}?page=${page}&size=${size}`)
  }

  getByTeacher(teacherId:number, page:number, size:number){
    return this.http.get<CourseEvaluation>(`${this.api_url}/paginated/teacher/${teacherId}?page=${page}&size=${size}`)
  }

}
