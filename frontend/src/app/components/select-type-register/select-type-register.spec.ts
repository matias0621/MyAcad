import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectTypeRegister } from './select-type-register';

describe('SelectTypeRegister', () => {
  let component: SelectTypeRegister;
  let fixture: ComponentFixture<SelectTypeRegister>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SelectTypeRegister]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SelectTypeRegister);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
