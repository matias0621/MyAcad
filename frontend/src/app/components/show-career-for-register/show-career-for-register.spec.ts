import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowCareerForRegister } from './show-career-for-register';

describe('ShowCareerForRegister', () => {
  let component: ShowCareerForRegister;
  let fixture: ComponentFixture<ShowCareerForRegister>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowCareerForRegister]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowCareerForRegister);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
