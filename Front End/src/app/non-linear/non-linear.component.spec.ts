import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NonLinearComponent } from './non-linear.component';

describe('NonLinearComponent', () => {
  let component: NonLinearComponent;
  let fixture: ComponentFixture<NonLinearComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NonLinearComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NonLinearComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
