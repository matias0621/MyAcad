import { Component, OnDestroy, OnInit } from '@angular/core';
import { CareerService } from '../../Services/CareerService/career-service';
import { CommissionService } from '../../Services/Commission/commission-service';
import Career from '../../Models/Users/Careers/Career';
import Technical from '../../Models/Users/Careers/Technical';
import Course from '../../Models/Users/Careers/Course';
import Commission from '../../Models/Commission/commission';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-inscription-to-commission',
  imports: [ReactiveFormsModule],
  templateUrl: './inscription-to-commission.html',
  styleUrl: './inscription-to-commission.css'
})
export class InscriptionToCommission implements OnInit, OnDestroy {

  listCommission: Commission[] = []
  form!:FormGroup
  legajo!:FormControl

  constructor(public careerService:CareerService, 
    public commisionService:CommissionService){}

  ngOnInit(): void {
    this.getCommisionByNameProgram()
  }

  ngOnDestroy(): void {
    this.careerService.setCareerSelected(null)
  }

  getCommisionByNameProgram(){
    
    const program:string = this.careerService.getCareerSelected() ?? "career"; 

    this.commisionService.getByProgram(program).subscribe({
      next: (res) => {
        this.listCommission = res
      }
    })
  }


}
