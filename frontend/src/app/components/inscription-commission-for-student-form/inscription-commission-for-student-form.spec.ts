import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InscriptionCommissionForStudentForm } from './inscription-commission-for-student-form';

describe('InscriptionCommissionForStudentForm', () => {
  let component: InscriptionCommissionForStudentForm;
  let fixture: ComponentFixture<InscriptionCommissionForStudentForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InscriptionCommissionForStudentForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InscriptionCommissionForStudentForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
