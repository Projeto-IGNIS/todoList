import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';


interface Board {
  id: string;
  ownerId: string;
  title: string;
}

@Injectable({
  providedIn: 'root',
})
export class BoardService {
  private readonly baseUrl = 'http://localhost:8080/board';

  private readonly http=inject(HttpClient);


  getAllBoards(): Observable<Board[]> {
    const ownerId = localStorage.getItem('ownerId');
    return this.http.get<Board[]>(`${this.baseUrl}/myBoards/${ownerId}`);
  }

  getOwnerId(): string | null {
    const ownerId = localStorage.getItem('ownerId');
    return ownerId;
  }

  createBoard(board: Board): Observable<Board> {
    return this.http.post<Board>(this.baseUrl + "/createBoard", board);
  }

  deleteBoard(id: string): Observable<string> {
    return this.http.delete(this.baseUrl + "/deleteBoard/" + id, { responseType: 'text' });
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
}
