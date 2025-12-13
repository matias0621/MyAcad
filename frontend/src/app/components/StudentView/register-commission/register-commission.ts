import { ChangeDetectorRef, Component } from '@angular/core';
import { InscriptionToCommission } from '../../../Models/InscriptionToCommission/InscritionToCommission';
import { InscriptionToCommissionService } from '../../../Services/InscriptionToCommission/inscription-to-commission-service';
import { DatePipe } from '@angular/common';
import { ProgramService } from '../../../Services/Program/program-service';
import { CommissionService } from '../../../Services/Commission/commission-service';
import { AuthService } from '../../../Services/Auth/auth-service';
import { NotificationService } from '../../../Services/notification/notification.service';
import Student from '../../../Models/Users/Student';

declare const bootstrap: { Modal: any };
@Component({
  selector: 'app-register-commission',
  imports: [DatePipe],
  templateUrl: './register-commission.html',
  styleUrl: './register-commission.css'
})
export class RegisterCommission {

  inscriptionList !: InscriptionToCommission[];
  token:any 

  constructor(
    public inscriptionService:InscriptionToCommissionService, 
    public programService:ProgramService,
    public commissionService: CommissionService,
    private authService:AuthService,
    private notificationService:NotificationService,
    private crd:ChangeDetectorRef){}

  ngOnInit(): void {
    this.getAllInscriptionOfStudent()
    this.token = this.authService.getDecodedToken()
  }
  
  getAllInscriptionOfStudent(){

    const token:any = this.authService.getDecodedToken()

    this.inscriptionService.getInscriptionByStudent(token.id).subscribe({
      next: (res) => {
        this.inscriptionList = res;
        console.log(res)

        this.crd.detectChanges()
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  registerByStudent(subjectId:number, commissionId:number){
    console.log(commissionId)
    this.commissionService.regiterByStudent(commissionId, subjectId).subscribe({
      next: () =>{
        this.notificationService.success("Te registraste correctamente")
      },
      error: (err) => {
        this.notificationService.error(err.error, true)
        console.log(err)
      }
    })
  }
}
