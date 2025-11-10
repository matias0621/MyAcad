import { Component } from '@angular/core';
import { SelectTypeRegister } from '../../components/select-type-register/select-type-register';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-select-type-register-page',
  imports: [SelectTypeRegister],
  templateUrl: './select-type-register-page.html',
  styleUrl: './select-type-register-page.css'
})
export class SelectTypeRegisterPage {

  nameCareer:string | undefined

  constructor(private actRoute:ActivatedRoute){
    this.nameCareer = this.actRoute.snapshot.params['name']
  }


}
