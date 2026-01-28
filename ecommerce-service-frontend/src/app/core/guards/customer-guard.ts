import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {AuthService} from '../services/auth.service';

export const customerGuard = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const isCustomer = authService.isCustomer();
  console.log('Guard check - Is Customer:', isCustomer);
  console.log('User Role in Storage:', authService.getUser()?.userRole);

  if(isCustomer){
    return true;
  }

  console.warn('Guard blocked access. Redirecting...');
  return router.parseUrl('/auth/login');
};
