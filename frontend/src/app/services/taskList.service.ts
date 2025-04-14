import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';


interface TaskList {
  id: string;
  name: string;
  boardId: string;
}
interface Task {
  title: string;
  status: string;
  description?: string;
  listId: string;
}

@Injectable({
  providedIn: 'root',
})
export class TaskListService {
  private readonly taskListUrl = 'http://localhost:8080/taskList';
  private readonly taskUrl = 'http://localhost:8080/task';
  

  private readonly http=inject(HttpClient);

  getAllTaskLists(boardId: string): Observable<TaskList[]> {
    return this.http.get<TaskList[]>(`${this.taskListUrl}/taskListByBoardId/${boardId}`);
  }
  
  getTaskByTaskListId(taskListId: string): Observable<TaskList[]> {
    return this.http.get<TaskList[]>(`${this.taskUrl}/taskByTaskListId/${taskListId}`);
  }

  addTask(task: Task): Observable<Task> {
    return this.http.post<Task>(`${this.taskUrl}/createTask`, task);
  }

  moveTask(task: any, destinationListId: string): void {

    this.deleteTask(task.id);
    task.listId = destinationListId;
    this.createTask(task);
}


  createTask(task: any): void {
    
    this.http.post(this.taskUrl + '/createTask', task).subscribe({
      next: () => {
        console.log('Task successfully added to new list.');
      },
      error: (error) => {
        console.error('Error adding task to new list:', error);
      }
    });
  }

  deleteTask(taskId: string): void {
    this.http.delete(`${this.taskUrl}/deleteTask/${taskId}`).subscribe({
      next: () => {},
      error: () => {}
    });
  }

  updateTaskTitle(taskId: string, newTitle: string): Observable<Task> {
    return this.http.put<Task>(`${this.taskUrl}/updateTaskTitle`, { id: taskId, title: newTitle });
  }
  updateTaskDescription(taskId: string, newDescription: string): Observable<Task> {
    return this.http.put<Task>(`${this.taskUrl}/updateTaskDescription`, { id: taskId, description: newDescription });
  }
  updateTaskStatus(taskId: string, newStatus: string): Observable<Task> {
    return this.http.put<Task>(`${this.taskUrl}/updateTaskStatus`, { id: taskId, status: newStatus });
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

  createTaskList(taskList: TaskList): Observable<TaskList> {
    return this.http.post<TaskList>(`${this.taskListUrl}/createTaskList`, taskList);
  }

  deleteTaskList(taskListId: string): Observable<void> {
    return this.http.delete<void>(`${this.taskListUrl}/deleteTaskList/${taskListId}`);
  }

  getBoardById(boardId: string): Observable<any> {
    return this.http.get<any>(`http://localhost:8080/board/getBoard/${boardId}`);
  }

  updateTaskListTitle(taskListId: string, newTitle: string): Observable<TaskList> {
    return this.http.put<TaskList>(`${this.taskListUrl}/updateTaskList`, {id: taskListId, name: newTitle });
  }

  updateBoardTitle(boardId: string, newTitle: string): Observable<any> {
    return this.http.put<any>(`http://localhost:8080/board/updateBoardTitle`, { id: boardId, title: newTitle });
  }
}
