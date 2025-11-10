import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectTypeRegisterPage } from './select-type-register-page';

describe('SelectTypeRegisterPage', () => {
  let component: SelectTypeRegisterPage;
  let fixture: ComponentFixture<SelectTypeRegisterPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SelectTypeRegisterPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SelectTypeRegisterPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
