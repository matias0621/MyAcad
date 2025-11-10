import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterToCareer } from './register-to-career';

describe('RegisterToCareer', () => {
  let component: RegisterToCareer;
  let fixture: ComponentFixture<RegisterToCareer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterToCareer]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterToCareer);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
