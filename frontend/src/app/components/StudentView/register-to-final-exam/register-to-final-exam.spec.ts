import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterToFinalExam } from './register-to-final-exam';

describe('RegisterToFinalExam', () => {
  let component: RegisterToFinalExam;
  let fixture: ComponentFixture<RegisterToFinalExam>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterToFinalExam]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterToFinalExam);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
