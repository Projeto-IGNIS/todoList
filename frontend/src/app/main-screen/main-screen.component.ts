import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DragDropModule, CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { ActivatedRoute,RouterModule } from '@angular/router';
import { TaskListService } from '../services/taskList.service';
import {MatIconModule} from '@angular/material/icon';
import {MatMenuModule} from '@angular/material/menu';
import {MatButtonModule} from '@angular/material/button';
import { FormsModule } from '@angular/forms';

interface TaskList {
  id: string;
  name: string;
  boardId: string;
  tasks?: any[];
}

interface Task {
  title: string;
  status: string;
  listId: string;
}


@Component({
  selector: 'app-main-screen',
  standalone: true,
  imports: [CommonModule, DragDropModule, RouterModule, MatButtonModule, MatMenuModule, MatIconModule, FormsModule],
  templateUrl: './main-screen.component.html',
  styleUrls: ['./main-screen.component.css']
})
export class MainScreenComponent {

  private boardId: string | null = null;
  taskLists: Array<TaskList> = [];
  connectedLists: string[] = [];

  showCreateTaskForm: boolean = false;
  newTaskTitle: string = '';
  newTaskDescription: string = '';
  selectedList: TaskList | null = null;

  private readonly router = inject(ActivatedRoute);
  private readonly taskListService = inject(TaskListService);  

  ngOnInit(): void {
    this.loadTaskLists();
  }

  loadTaskLists(): void {
    this.boardId = this.router.snapshot.queryParams['boardId'];
    this.taskListService.getAllTaskLists(this.boardId!).subscribe({
      next: (data: Array<TaskList>) => {
        this.taskLists = data.map(list => ({
          ...list,
          tasks: list.tasks ?? []
        }));
        
        let i = 0;
        for (const taskList of this.taskLists) {
          
          this.taskListService.getTaskByTaskListId(taskList.id).subscribe({
            next: (tasks: any) => {
              this.taskLists[i].tasks = tasks;
              i++;
            },
            error: (err) => {
              console.error('Erro ao carregar tarefas:', err);
            },
          });
        }


        this.connectedLists = this.taskLists.map(list => list.name);
        console.log('Connected lists:', this.connectedLists);
        console.log('Task lists loaded:', this.taskLists);
      },
      error: (err) => {
        console.error('Erro ao carregar listas:', err);
      }
    });
  }

  getTasks(taskListId: string): any[] | undefined {
    const taskList = this.taskLists.find(list => list.id === taskListId);
    return taskList ? taskList.tasks : undefined;
  }
  
  getConnectedListIds(currentListId: string): string[] {
    return this.taskLists
      .filter(list => list.id !== currentListId)
      .map(list => list.name);
  }
  
  
  drop(event: CdkDragDrop<any[] | undefined>) {
    if (!event.container.data) {
      console.error('Erro: container.data está undefined');
      return;
    }

    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      if (!event.previousContainer.data) {
        console.error('Erro: previousContainer.data está undefined');
        return;
      }
      
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );

      const taskDestination = this.taskLists.find(list => list.tasks === event.container.data)?.id;
      this.moveTask(event.container.data[event.currentIndex], taskDestination);

    }
  }
  
  moveTask(task: any, taskIdDestination: any): void {
    this.taskListService.moveTask(task, taskIdDestination);
  }


  toggleCreateTaskForm(list?: TaskList): void {
    this.showCreateTaskForm = !this.showCreateTaskForm;
    this.newTaskTitle = '';
    this.newTaskDescription = '';
    this.selectedList = list || null;
  }

  createTask(list: TaskList | null): void {
    if (!list) {
      console.error('No list selected for task creation.');
      return;
    }

    if (!this.newTaskTitle.trim()) {
      alert('O título é obrigatório!');
      return;
    }

    const newTask: Task = {
      title: this.newTaskTitle,
      status: 'pending',
      listId: list.id
    };

    this.taskListService.addTask(newTask).subscribe({
      next: (task) => {
        list.tasks?.push(task);
        console.log('Task created:', task);
      },
      error: (err) => {
        console.error('Failed to create task:', err);
      }
    });

    this.toggleCreateTaskForm();
  }
}