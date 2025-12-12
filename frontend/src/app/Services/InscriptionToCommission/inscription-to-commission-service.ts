import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { InscriptionToCommission, PostInscriptionToCommission } from '../../Models/InscriptionToCommission/InscritionToCommission';

@Injectable({
  providedIn: 'root'
})
export class InscriptionToCommissionService {
  readonly apiUrl = "http://localhost:8080/inscriptions/commission"

  constructor(private http:HttpClient){}

  getAllInscriptions(){
    return this.http.get<InscriptionToCommission[]>(this.apiUrl)
  }

  getInscriptionById(id:number){
    return this.http.get<InscriptionToCommission>(`${this.apiUrl}/${id}`)
  }

  getByInscriptionDate(date:string){
    return this.http.get<InscriptionToCommission[]>(`${this.apiUrl}/inscriptions/${date}`)
  }

  getByFinishInscriptionDate(date:string){
    return this.http.get<InscriptionToCommission[]>(`${this.apiUrl}/finishDate/${date}`)
  }

  getInscriptionByCommissionId(commissionId:string){
    return this.http.get<InscriptionToCommission[]>(`${this.apiUrl}/find/${commissionId}`)
  }

  saveInscription(inscription:PostInscriptionToCommission){
    return this.http.post(this.apiUrl, inscription)
  }

  updateInscription(inscriptionId:number, inscription:PostInscriptionToCommission){
    return this.http.put(`${this.apiUrl}/${inscriptionId}`, inscription)
  }

  deleteInscription(inscriptionId:number){
    return this.http.delete(`${this.apiUrl}/${inscriptionId}`)
  }

}
