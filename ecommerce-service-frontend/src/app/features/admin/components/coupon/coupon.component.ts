import {ChangeDetectorRef, Component, inject} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Coupon} from '../../../../shared/models/coupon.model';
import {CouponService} from '../../services/coupon.service';
import {DatePipe, NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-coupon',
  imports: [
    FormsModule,
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    DatePipe
  ],
  templateUrl: './coupon.component.html',
  styleUrl: './coupon.component.css',
})
export class CouponComponent {
  couponForm! : FormGroup;
  coupons: Coupon[] = [];
  private fb = inject(FormBuilder);
  private couponService = inject(CouponService);
  private cdr = inject(ChangeDetectorRef);
  isSaving = false;

  constructor() {}

  ngOnInit(): void {
    this.initForm();
    this.loadCoupons();
  }

  loadCoupons(): void {
    this.couponService.getAllCouponsForAdmin().subscribe({
      next: (data) => {
        // The spread operator [...] ensures Angular sees a NEW array reference
        this.coupons = [...data];
        this.cdr.detectChanges();
      },
      error: (err) => console.error("API Error:", err)
    });
  }

  initForm(){
    this.couponForm = this.fb.group({
      name: ['', [Validators.required]],
      code: ['', [Validators.required]],
      discount: [null, [Validators.required]],
      expirationDate: [null, [Validators.required]],
    });
  }

  onSubmit() {
    if (this.couponForm.invalid) return;

    const couponData: Coupon = this.couponForm.value;

    this.couponService.addNewCoupon(couponData)
                      .subscribe({next: () => {
                        this.resetForm();
                        this.loadCoupons();
                      },
                      error: (err) => console.error("Could not save coupon", err)
    });
  }

  resetForm() {
    this.couponForm.reset();
    this.loadCoupons();
  }
}
