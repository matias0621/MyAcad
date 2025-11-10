import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubjectsTeacherView } from './subjects-teacher-view';

describe('SubjectsTeacherView', () => {
  let component: SubjectsTeacherView;
  let fixture: ComponentFixture<SubjectsTeacherView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubjectsTeacherView]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubjectsTeacherView);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
