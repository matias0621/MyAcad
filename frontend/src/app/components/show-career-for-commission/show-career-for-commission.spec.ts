import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowCareerForCommission } from './show-career-for-commission';

describe('ShowCareerForCommission', () => {
  let component: ShowCareerForCommission;
  let fixture: ComponentFixture<ShowCareerForCommission>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowCareerForCommission]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowCareerForCommission);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
