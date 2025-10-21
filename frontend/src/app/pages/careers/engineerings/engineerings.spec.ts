import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Engineerings } from './engineerings';

describe('Engineerings', () => {
  let component: Engineerings;
  let fixture: ComponentFixture<Engineerings>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Engineerings]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Engineerings);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
