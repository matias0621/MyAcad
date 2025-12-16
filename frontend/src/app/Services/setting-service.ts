import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SettingService {
  readonly api_url = "http://localhost:8080/settings"

  constructor(private http:HttpClient){}

  isCourseEvaluationEnabled(){
    return this.http.get<boolean>(`${this.api_url}/course-evaluations`)
  }

  setCourseEvaluationEnabled(enabled: boolean) {
    return this.http.put(
      `${this.api_url}/course-evaluations?enabled=${enabled}`,
      null
    );
  }
}
