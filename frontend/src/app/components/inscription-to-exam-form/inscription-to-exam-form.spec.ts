import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InscriptionToExamForm } from './inscription-to-exam-form';

describe('InscriptionToExamForm', () => {
  let component: InscriptionToExamForm;
  let fixture: ComponentFixture<InscriptionToExamForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InscriptionToExamForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InscriptionToExamForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
