import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  InscriptionToFinalExam,
  PostInscriptionToFinalExam,
} from '../../Models/InscriptionToFinalExam/InscriptionToFinalExam';

@Injectable({
  providedIn: 'root',
})
export class InscriptionToFinalExamService {
  readonly api_url = 'http://localhost:8080/inscription-final-exam';

  inscriptionList: InscriptionToFinalExam[] = [];

  constructor(private http: HttpClient) {}

  getAllInscription() {
    return this.http.get<InscriptionToFinalExam[]>(this.api_url);
  }

  getInscriptionById(id: number) {
    return this.http.get<InscriptionToFinalExam>(`${this.api_url}/${id}`);
  }

  getInscripionsBySubjectsId(subjectsId: number) {
    return this.http.get<InscriptionToFinalExam[]>(`${this.api_url}/subjects/${subjectsId}`);
  }

  getInscriptionByDateInscription(inscriptionDate: string) {
    return this.http.get<InscriptionToFinalExam[]>(
      `${this.api_url}/date-inscription/${inscriptionDate}`
    );
  }

  getInscriptionByDateExam(examDate: string) {
    return this.http.get<PostInscriptionToFinalExam[]>(
      `${this.api_url}/date-inscription/${examDate}`
    );
  }

  postInscriptionToFinal(inscription: PostInscriptionToFinalExam) {
    return this.http.post(this.api_url, inscription);
  }

  putInscriptionToFinal(inscriptionUpdate: PostInscriptionToFinalExam, idInscription: number) {
    return this.http.put(`${this.api_url}/${idInscription}`, inscriptionUpdate);
  }

  addStudentToFinalExam(idInscription: number) {
    return this.http.put(`${this.api_url}/register-student-for-exam/${idInscription}`, {});
  }

  deleteInscription(idInscription: number) {
    return this.http.delete(`${this.api_url}/${idInscription}`);
  }
}
