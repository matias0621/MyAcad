import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubjectFormEdit } from './subject-form-edit';

describe('SubjectFormEdit', () => {
  let component: SubjectFormEdit;
  let fixture: ComponentFixture<SubjectFormEdit>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubjectFormEdit]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubjectFormEdit);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
