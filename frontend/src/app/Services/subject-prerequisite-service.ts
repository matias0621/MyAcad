import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PostSubjectPrerequisite, SubjectPrerequisite } from '../Models/SubjectPrerequisite/SubjectPrerequisite';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SubjectPrerequisiteService {

  private readonly API_URL = 'http://localhost:8080/SubjectPrerrequisite';

  constructor(private http: HttpClient) {}

  // ===================== CREATE =====================

  create(dto: PostSubjectPrerequisite): Observable<void> {
    return this.http.post<void>(this.API_URL, dto);
  }

  // ===================== UPDATE =====================

  update(id: number, dto: PostSubjectPrerequisite): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/${id}`, dto);
  }

  // ===================== DELETE =====================

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  // ===================== READ =====================

  findAll(): Observable<SubjectPrerequisite[]> {
    return this.http.get<SubjectPrerequisite[]>(this.API_URL);
  }

  findById(id: number): Observable<SubjectPrerequisite> {
    return this.http.get<SubjectPrerequisite>(`${this.API_URL}/${id}`);
  }

  // ===================== FILTERS =====================

  findBySubject(subjectId: number): Observable<SubjectPrerequisite[]> {
    return this.http.get<SubjectPrerequisite[]>(
      `${this.API_URL}/subject/${subjectId}`
    );
  }

  findByPrerequisite(prerequisiteId: number): Observable<SubjectPrerequisite[]> {
    return this.http.get<SubjectPrerequisite[]>(
      `${this.API_URL}/prerequisite/${prerequisiteId}`
    );
  }

  findByRequiredStatus(status: string): Observable<SubjectPrerequisite[]> {
    return this.http.get<SubjectPrerequisite[]>(
      `${this.API_URL}/status/${status}`
    );
  }

  // ===================== SPECIAL =====================

  exists(subjectId: number, prerequisiteId: number): Observable<boolean> {
    const params = new HttpParams()
      .set('subjectId', subjectId)
      .set('prerequisiteId', prerequisiteId);

    return this.http.get<boolean>(
      `${this.API_URL}/exists`,
      { params }
    );
  }

  findBySubjectAndPrerequisite(
    subjectId: number,
    prerequisiteId: number
  ): Observable<SubjectPrerequisite> {

    const params = new HttpParams()
      .set('subjectId', subjectId)
      .set('prerequisiteId', prerequisiteId);

    return this.http.get<SubjectPrerequisite>(
      `${this.API_URL}/subject-prerequisite`,
      { params }
    );
  }

  findByProgram(program: string): Observable<SubjectPrerequisite[]> {
    return this.http.get<SubjectPrerequisite[]>(
      `${this.API_URL}/program/${program}`
    );
  }

  findSubjectsRequiringStatus(
    program: string,
    status: string
  ): Observable<SubjectPrerequisite[]> {
    return this.http.get<SubjectPrerequisite[]>(
      `${this.API_URL}/program/${program}/status/${status}`
    );
  }
  
}
