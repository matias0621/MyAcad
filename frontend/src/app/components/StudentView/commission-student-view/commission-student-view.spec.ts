import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommissionStudentView } from './commission-student-view';

describe('CommissionStudentView', () => {
  let component: CommissionStudentView;
  let fixture: ComponentFixture<CommissionStudentView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CommissionStudentView]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CommissionStudentView);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
