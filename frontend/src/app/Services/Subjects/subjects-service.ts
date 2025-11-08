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

  getAllSubjectWithSemesterLessThan(semester:number){
    return this.http.get<Subjects[]>(`${this.api_url}/semester-less-than/${semester}`)
  }

  getAllSubjectByName(name:string){
    return this.http.get<Subjects[]>(`${this.api_url}/search?name=${name}`)
  }

  getSubjectById(id:string){
    return this.http.get<Subjects>(`${this.api_url}/${id}`)
  }

  getByProgram(program: string) {
      return this.http.get<Subjects[]>(`${this.api_url}/program/${program}`);
    }

  postSubject(subject:Subjects){
    subject.subjectActive = true;
    return this.http.post(this.api_url, subject)
  }

  putSubject(subject:Subjects){
    return this.http.put(`${this.api_url}/${subject.id}`, subject)
  }

  addSubjectToCareer(nameCareer:string, subjects:Subjects){
    return this.http.put(`${this.api_url}/add-subject-to-career/${nameCareer}`, subjects)
  }

  deleteSubjectToCareer(nameCareer:string, subjects:Subjects){
    return this.http.put(`${this.api_url}/delete-subject-to-career/${nameCareer}`, subjects)
  }

  deleteSubject(id:number){
    return this.http.delete(`${this.api_url}/${id}`)
  }

  definitiveDeleteSubject(id:number){
    return this.http.delete(`${this.api_url}/delete/${id}`)
  }

}
