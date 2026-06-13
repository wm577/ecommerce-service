import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerorderdetailsComponent } from './customerorderdetails.component';

describe('CustomerorderdetailsComponent', () => {
  let component: CustomerorderdetailsComponent;
  let fixture: ComponentFixture<CustomerorderdetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustomerorderdetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CustomerorderdetailsComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
