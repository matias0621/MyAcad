import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Courses } from '../../pages/courses/courses';

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  readonly API_URL = 'http://localhost:8080/courses';

  constructor(
    private http: HttpClient
  ) { }

    getCourses() {
      return this.http.get<Courses[]>(this.API_URL);
    }

    postCourse(course: Courses) {
      return this.http.post<Courses>(this.API_URL, course);
    }

    deleteCourse(id: number) {
      return this.http.delete<Courses>(`${this.API_URL}/${id}`);
    }
  
}
