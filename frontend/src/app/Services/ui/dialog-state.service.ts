import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class DialogStateService {
  private changePasswordSubject = new BehaviorSubject<boolean>(false);
  open$ = this.changePasswordSubject.asObservable();

  openChangePassword(){
    this.changePasswordSubject.next(true);
  }

  closeChangePassword(){
    this.changePasswordSubject.next(false);
  }
}
