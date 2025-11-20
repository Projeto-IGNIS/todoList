import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TaskList } from '../models/task-list.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TaskListService {
  private readonly API_URL = `${environment.apiUrl}/taskList`;

  constructor(private http: HttpClient) {}

  createTaskList(taskList: TaskList): Observable<string> {
    return this.http.post(`${this.API_URL}/createTaskList`, taskList, { responseType: 'text' });
  }

  getTaskList(taskListId: number): Observable<TaskList> {
    return this.http.get<TaskList>(`${this.API_URL}/${taskListId}`);
  }

  getAllTaskLists(): Observable<TaskList[]> {
    return this.http.get<TaskList[]>(`${this.API_URL}/allTaskLists`);
  }

  updateTaskList(taskList: TaskList): Observable<TaskList> {
    return this.http.put<TaskList>(`${this.API_URL}/updateTaskList`, taskList);
  }

  updateBoardId(taskList: TaskList): Observable<TaskList> {
    return this.http.put<TaskList>(`${this.API_URL}/updateBoardId`, taskList);
  }

  deleteTaskList(taskListId: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/deleteTaskList/${taskListId}`);
  }
}
