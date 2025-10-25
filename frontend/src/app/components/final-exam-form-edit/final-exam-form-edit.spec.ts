import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinalExamFormEdit } from './final-exam-form-edit';

describe('FinalExamFormEdit', () => {
  let component: FinalExamFormEdit;
  let fixture: ComponentFixture<FinalExamFormEdit>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FinalExamFormEdit]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FinalExamFormEdit);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
