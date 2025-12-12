import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InscriptionCommissionForStudentFormEdit } from './inscription-commission-for-student-form-edit';

describe('InscriptionCommissionForStudentFormEdit', () => {
  let component: InscriptionCommissionForStudentFormEdit;
  let fixture: ComponentFixture<InscriptionCommissionForStudentFormEdit>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InscriptionCommissionForStudentFormEdit]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InscriptionCommissionForStudentFormEdit);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
