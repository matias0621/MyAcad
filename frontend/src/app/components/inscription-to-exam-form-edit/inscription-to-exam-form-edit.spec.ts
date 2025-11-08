import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InscriptionToExamFormEdit } from './inscription-to-exam-form-edit';

describe('InscriptionToExamFormEdit', () => {
  let component: InscriptionToExamFormEdit;
  let fixture: ComponentFixture<InscriptionToExamFormEdit>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InscriptionToExamFormEdit]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InscriptionToExamFormEdit);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
