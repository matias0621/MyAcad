import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterStudentCareer } from './register-student-career';

describe('RegisterStudentCareer', () => {
  let component: RegisterStudentCareer;
  let fixture: ComponentFixture<RegisterStudentCareer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterStudentCareer]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterStudentCareer);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
