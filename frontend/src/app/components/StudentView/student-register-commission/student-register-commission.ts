import { Component, OnInit } from '@angular/core';
import { CommissionService } from '../../../Services/Commission/commission-service';
import { ActivatedRoute } from '@angular/router';
import Commission from '../../../Models/Commission/commission';
import { NotificationService } from '../../../Services/notification/notification.service';

@Component({
  selector: 'app-student-register-commission',
  imports: [],
  templateUrl: './student-register-commission.html',
  styleUrl: './student-register-commission.css'
})
export class StudentRegisterCommission implements OnInit {


  listCommission:Commission[] = []
  programName!:string

  constructor(
    public comService:CommissionService,
    public notification:NotificationService,
    public actRouter:ActivatedRoute
  ){
    this.programName = this.actRouter.snapshot.params['name']
  }

  ngOnInit(): void {
    this.getCommission()
  }

  getCommission(){
    this.comService.getCommissionNotEnrolled(this.programName).subscribe({
      next: (res) => {
        this.listCommission = res
        console.log(res)
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  registerStudent(comId:number, subId:number){
    this.comService.regiterByStudent(comId, subId).subscribe({
      next: (res) => {
        this.notification.success("Te registraste exitosamente")
        this.getCommission()
      },
      error: (err) => {
        this.notification.error("Hubo un error, intentelo mas tarde", true)
        console.log(err)
      }
    })
  }


}
