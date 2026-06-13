import { Routes } from '@angular/router';
import {CategoryComponent} from './features/admin/components/category/category.component';
import {ProductComponent} from './features/admin/components/product/product.component';
import {CouponComponent} from './features/admin/components/coupon/coupon.component';
import {ProductlistComponent} from './features/customer/components/productlist/productlist.component';
import {CartitemsComponent} from './features/customer/components/cartitems/cartitems.component';
import {CustomerorderComponent} from './features/customer/components/customerorder/customerorder.component';
import {
  CustomerorderdetailsComponent
} from './features/customer/components/customerorderdetails/customerorderdetails.component';
import {AdminorderComponent} from './features/admin/components/adminorder/adminorder.component';
import {AdminorderdetailsComponent} from './features/admin/components/adminorderdetails/adminorderdetails.component';
import {AdminanalyticsComponent} from './features/admin/components/adminanalytics/adminanalytics.component';
import {LoginComponent} from './features/auth/login/login.component';
import {adminGuard} from './core/guards/admin-guard';
import {customerGuard} from './core/guards/customer-guard';
import {SignupComponent} from './features/auth/signup/signup.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'auth/login',
    pathMatch: 'full'
  },
  {
    path: 'auth/login',
    component: LoginComponent,
  },
  {
    path: 'auth/signup',
    component: SignupComponent,
  },
  {
    path: 'admin',
    canActivate: [adminGuard],
    children: [
      {
        path: 'order',
        component: AdminorderComponent,
      },
      { path: 'order/:id',
        component: AdminorderdetailsComponent
      },
      { path: 'analytics',
        component: AdminanalyticsComponent
      },
      {
        path: 'category',
        component: CategoryComponent
      },
      {
        path: 'product',
        component: ProductComponent,
      },
      {
        path: 'coupon',
        component: CouponComponent,
      },
    ]
  },

  {
    path: 'customer',
    canActivate: [customerGuard],
    children: [
    {
      path: 'products',
      component: ProductlistComponent
    },
    {
      path: 'cart',
      component: CartitemsComponent,
    },
    {
      path: 'order/:id',
      component: CustomerorderdetailsComponent,
    },
    {
      path: 'order',
      component: CustomerorderComponent
    },
    ]
  },
  { path: '**', redirectTo: 'auth/login' }
];
