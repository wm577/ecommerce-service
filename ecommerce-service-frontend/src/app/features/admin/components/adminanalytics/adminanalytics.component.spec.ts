import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminanalyticsComponent } from './adminanalytics.component';

describe('AdminanalyticsComponent', () => {
  let component: AdminanalyticsComponent;
  let fixture: ComponentFixture<AdminanalyticsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminanalyticsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminanalyticsComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
