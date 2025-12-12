import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterCommission } from './register-commission';

describe('RegisterCommission', () => {
  let component: RegisterCommission;
  let fixture: ComponentFixture<RegisterCommission>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterCommission]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterCommission);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
