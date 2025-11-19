import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class SignUpService {
  private readonly apiUrl = environment.apiUrl + '/users/createUser';

  constructor(private readonly http: HttpClient) {}

  register(user: User): Observable<User> {
    return this.http.post<User>(this.apiUrl, user);
  }
}
