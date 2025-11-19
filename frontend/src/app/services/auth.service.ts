import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { LoginRequest, User } from '../models/user.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = environment.apiUrl;
  private tokenSubject = new BehaviorSubject<string | null>(this.getStoredToken());
  private userEmailSubject = new BehaviorSubject<string | null>(this.getStoredEmail());

  public token$ = this.tokenSubject.asObservable();
  public userEmail$ = this.userEmailSubject.asObservable();

  constructor(private http: HttpClient) {}

  login(credentials: LoginRequest): Observable<string> {
    return this.http.post(`${this.API_URL}/auth/login`, credentials, { responseType: 'text' })
      .pipe(
        tap(token => {
          this.setToken(token);
          this.setEmail(credentials.email);
        })
      );
  }

  register(user: User): Observable<User> {
    return this.http.post<User>(`${this.API_URL}/users/createUser`, user);
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('userId');
    this.tokenSubject.next(null);
    this.userEmailSubject.next(null);
  }

  getOwnerId(email: string): Observable<string> {
    return this.http.get(`${this.API_URL}/auth/ownerId/${email}`, { responseType: 'text' })
      .pipe(
        tap(id => {
          localStorage.setItem('userId', id);
        })
      );
  }

  isAuthenticated(): boolean {
    return !!this.getStoredToken();
  }

  getToken(): string | null {
    return this.getStoredToken();
  }

  private setToken(token: string): void {
    localStorage.setItem('token', token);
    this.tokenSubject.next(token);
  }

  private setEmail(email: string): void {
    localStorage.setItem('userEmail', email);
    this.userEmailSubject.next(email);
  }

  private getStoredToken(): string | null {
    return localStorage.getItem('token');
  }

  private getStoredEmail(): string | null {
    return localStorage.getItem('userEmail');
  }

  getUserId(): string | null {
    return localStorage.getItem('userId');
  }
}
