import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentTicket } from './student-ticket';

describe('StudentTicket', () => {
  let component: StudentTicket;
  let fixture: ComponentFixture<StudentTicket>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StudentTicket]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudentTicket);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
