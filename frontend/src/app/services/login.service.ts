import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private readonly apiUrl = 'http://localhost:8080/'; // URL do backend

  private readonly http=inject(HttpClient);
  
  getAllUsers(): Observable<string> {
    return this.http.get(this.apiUrl+"users/all", { responseType: 'text' });
  }

  login(user: { email: string; password: string }) {
    console.log("TA AI");
    return this.http.post(this.apiUrl+"auth/login", user, { responseType: 'text' });

  }

  clearLocalStorage(): void {
    debugger;
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('ownerId');
  }


  getOwnerId(email: string): Observable<string> {
    return this.http.get(this.apiUrl+"auth/ownerId/"+email, { responseType: 'text' });
  }

  saveToken(token: string): void {
    localStorage.setItem('jwtToken', token);
  }

  saveOwnerId(ownerId: string): void {
    localStorage.setItem('ownerId', ownerId);
  }
  

  getToken(): string | null {
    return localStorage.getItem('jwtToken');
  }

  getAuthenticatedHeaders(): HttpHeaders {
    const token = this.getToken();
    return new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  }

  getProtectedResource(): Observable<any> {
    const headers = this.getAuthenticatedHeaders();
    return this.http.get(this.apiUrl + "protected/resource", { headers });
  }

}
