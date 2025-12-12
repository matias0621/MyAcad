import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InscriptionCommissionForStudentList } from './inscription-commission-for-student-list';

describe('InscriptionCommissionForStudentList', () => {
  let component: InscriptionCommissionForStudentList;
  let fixture: ComponentFixture<InscriptionCommissionForStudentList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InscriptionCommissionForStudentList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InscriptionCommissionForStudentList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
