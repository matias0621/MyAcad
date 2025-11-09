import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeacherView } from './teacher-view';

describe('TeacherView', () => {
  let component: TeacherView;
  let fixture: ComponentFixture<TeacherView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TeacherView]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TeacherView);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
