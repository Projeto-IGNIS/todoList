import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SignUpService {
  private readonly apiUrl = 'http://localhost:8080/users'; // URL do backend

  private readonly http=inject(HttpClient);
  
  signUp(user: {name: string; email: string; password: string }) {
    return this.http.post(this.apiUrl+"/createUser", user, { responseType: 'text' });

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
