import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinalExamForm } from './final-exam-form';

describe('FinalExamForm', () => {
  let component: FinalExamForm;
  let fixture: ComponentFixture<FinalExamForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FinalExamForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FinalExamForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
