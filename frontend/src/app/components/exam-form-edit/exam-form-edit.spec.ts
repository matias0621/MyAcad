import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExamFormEdit } from './exam-form-edit';

describe('ExamFormEdit', () => {
  let component: ExamFormEdit;
  let fixture: ComponentFixture<ExamFormEdit>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExamFormEdit]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExamFormEdit);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
