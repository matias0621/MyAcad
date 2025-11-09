import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentRegisterCommission } from './student-register-commission';

describe('StudentRegisterCommission', () => {
  let component: StudentRegisterCommission;
  let fixture: ComponentFixture<StudentRegisterCommission>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StudentRegisterCommission]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudentRegisterCommission);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
