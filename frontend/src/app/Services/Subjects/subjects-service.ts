import { Subject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import Subjects from '../../Models/Subjects/Subjects';

@Injectable({
  providedIn: 'root'
})
export class SubjectsService {
  
  readonly api_url = "http://localhost:8080/subject"

  listSubject:Subjects[] = []

  constructor(private http:HttpClient){}


  getAllSubject(){
    return this.http.get<Subjects[]>(this.api_url)
  }

  getAllSubjectByName(name:string){
    return this.http.get<Subjects[]>(`${this.api_url}/search?name=${name}`)
  }

  postSubject(subject:Subjects){
    return this.http.post(this.api_url, subject)
  }

  putSubject(subject:Subjects){
    return this.http.put(`${this.api_url}/${subject.id}`, subject)
  }

  deleteSubject(id:string){
    return this.http.delete(`${this.api_url}/${id}`)
  }

}
