import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExamsFormEdit } from './exams-form-edit';

describe('ExamsFormEdit', () => {
  let component: ExamsFormEdit;
  let fixture: ComponentFixture<ExamsFormEdit>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExamsFormEdit]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExamsFormEdit);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
