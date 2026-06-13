import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerorderComponent } from './customerorder.component';

describe('CustomerorderComponent', () => {
  let component: CustomerorderComponent;
  let fixture: ComponentFixture<CustomerorderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustomerorderComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CustomerorderComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
