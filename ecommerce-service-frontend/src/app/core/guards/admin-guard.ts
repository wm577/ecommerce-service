import {Router} from '@angular/router';
import {AuthService} from '../services/auth.service';
import {inject} from '@angular/core';

export const adminGuard = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if(authService.isAdmin()){
    return true;
  }
  return router.parseUrl('/auth/signup');
};
