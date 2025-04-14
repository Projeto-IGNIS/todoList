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
  private readonly http = inject(HttpClient);

  private static readonly OWNER_ID_KEY = 'ownerId';
  private static readonly TOKEN_KEY = 'jwtToken';

  private getLocalStorageItem(key: string): string | null {
    return localStorage.getItem(key);
  }

  private buildUrl(endpoint: string): string {
    return `${this.baseUrl}/${endpoint}`;
  }

  public getOwnerId(): string | null {
    return this.getLocalStorageItem(BoardService.OWNER_ID_KEY);
  }

  private getToken(): string | null {
    return this.getLocalStorageItem(BoardService.TOKEN_KEY);
  }

  private getAuthenticatedHeaders(): HttpHeaders {
    const token = this.getToken();
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }

  getAllBoards(): Observable<Board[]> {
    const ownerId = this.getOwnerId();
    return this.http.get<Board[]>(this.buildUrl(`myBoards/${ownerId}`));
  }

  createBoard(board: Board): Observable<Board> {
    return this.http.post<Board>(this.buildUrl('createBoard'), board);
  }

  deleteBoard(id: string): Observable<string> {
    return this.http.delete(this.buildUrl(`deleteBoard/${id}`), { responseType: 'text' });
  }
}
