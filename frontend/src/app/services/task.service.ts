import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Task, CreateTaskRequest } from '../models/task.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private readonly API_URL = `${environment.apiUrl}/task`;

  constructor(private http: HttpClient) {}

  createTask(task: CreateTaskRequest): Observable<string> {
    return this.http.post(`${this.API_URL}/createTask`, task, { responseType: 'text' });
  }

  getTask(taskId: number): Observable<Task> {
    return this.http.get<Task>(`${this.API_URL}/getTask/${taskId}`);
  }

  getAllTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.API_URL}/getAllTasks`);
  }

  updateTaskTitle(task: Task): Observable<Task> {
    return this.http.put<Task>(`${this.API_URL}/updateTaskTitle`, task);
  }

  deleteTask(taskId: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/deleteTask/${taskId}`);
  }

  checkOverdueTasks(taskId: number): Observable<string> {
    return this.http.get(`${this.API_URL}/checkOverdueTasks/${taskId}`, { responseType: 'text' });
  }
}
