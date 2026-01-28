import { Injectable } from '@angular/core';
import {API_ENDPOINTS} from '../Config';
import {HttpClient} from '@angular/common/http';
import {SignupRequest} from '../../shared/models/signup-request.model';
import {Observable, tap} from 'rxjs';
import {User} from '../../shared/models/user.model';
import {LoginRequest} from '../../shared/models/login-request.model';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly API_URL = API_ENDPOINTS.auth;

  constructor(private http: HttpClient, private router: Router) {}

  signUp(signupData : SignupRequest) : Observable<User>{
    return this.http.post<User>(`${this.API_URL}/signup`, signupData);
  }

  login(credentials: LoginRequest): Observable<User> {
    return this.http.post<User>(`${this.API_URL}/login`, credentials).pipe(
      tap(user => {
        localStorage.setItem('currentUser', JSON.stringify(user));
      })
    );
  }

  // isLoggedIn(): boolean {
  //   return !!this.getUser();
  // }

  public getUser() : User | null{
    const user = localStorage.getItem('currentUser');
    return user? JSON.parse(user) : null;
  }

  isAdmin() : boolean{
    const user = this.getUser();
    return user ?.userRole === 'ADMIN';
  }

  isCustomer() : boolean{
    const user = this.getUser();
    return user ?.userRole === 'CUSTOMER'
  }

  logout(){
    localStorage.removeItem('currentUser');
    this.router.navigate(['/auth/login']);
  }
}
