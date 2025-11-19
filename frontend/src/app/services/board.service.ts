import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Board, CreateBoardRequest } from '../models/board.model';
import { TaskList } from '../models/task-list.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BoardService {
  private readonly API_URL = `${environment.apiUrl}/board`;

  constructor(private http: HttpClient) {}

  createBoard(board: CreateBoardRequest): Observable<Board> {
    return this.http.post<Board>(`${this.API_URL}/createBoard`, board);
  }

  getBoard(boardId: number): Observable<Board> {
    return this.http.get<Board>(`${this.API_URL}/getBoard/${boardId}`);
  }

  getAllBoards(): Observable<Board[]> {
    return this.http.get<Board[]>(`${this.API_URL}/allBoards`);
  }

  getMyBoards(ownerId: number): Observable<Board[]> {
    return this.http.get<Board[]>(`${this.API_URL}/myBoards/${ownerId}`);
  }

  isFavorite(boardId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.API_URL}/isFavorite/${boardId}`);
  }

  getTaskListsByBoard(boardId: number): Observable<TaskList[]> {
    return this.http.get<TaskList[]>(`${this.API_URL}/myTasksByBoard/${boardId}`);
  }

  updateBoardTitle(board: Board): Observable<Board> {
    return this.http.put<Board>(`${this.API_URL}/updateBoardTitle`, board);
  }

  toggleFavorite(boardId: number): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/toggleFavorite/${boardId}`, {});
  }

  deleteBoard(boardId: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/deleteBoard/${boardId}`);
  }
}
