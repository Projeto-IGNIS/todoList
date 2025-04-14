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
  id: string; // Add the id property
  title: string;
  status: string;
  description?: string;
  listId: string;
  dueDate?: string;
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
  public boardName: string = '';
  taskLists: Array<TaskList> = [];
  connectedLists: string[] = [];

  showCreateTaskForm: boolean = false;
  newTaskTitle: string = '';
  newTaskDescription: string = '';
  newTaskDueDate: string = '';
  selectedList: TaskList | null = null;

  showCreateListForm: boolean = false;
  newListName: string = '';

  showRenameListForm: boolean = false;
  renameListName: string = '';
  selectedListToRename: TaskList | null = null;

  showRenameBoardForm: boolean = false;
  renameBoardName: string = '';

  showEditTaskForm: boolean = false;
  editTask: Task | null = null;
  categories: string[] = ['Work', 'Personal', 'Urgent'];
  selectedCategory: string = '';
  dueDate: string = '';

  private readonly router = inject(ActivatedRoute);
  private readonly taskListService = inject(TaskListService);  

  ngOnInit(): void {
    this.loadBoardName();
    this.loadTaskLists();
  }

  loadBoardName(): void {
    this.boardId = this.router.snapshot.queryParams['boardId'];
    if (this.boardId) {
      this.taskListService.getBoardById(this.boardId).subscribe({
        next: (board) => {
          this.boardName = board.title; // Set the board name
        },
        error: (err) => {
          console.error('Failed to load board name:', err);
        }
      });
    }
  }

  loadTaskLists(): void {
    const boardId = this.router.snapshot.queryParams['boardId'];
    if (!boardId) {
      console.error('Board ID not found');
      return;
    }

    this.taskListService.getAllTaskLists(boardId).subscribe({
      next: (taskLists: TaskList[]) => {
        this.taskLists = taskLists.map(taskList => ({
          ...taskList,
          tasks: taskList.tasks || []
        }));

        this.taskLists.forEach(taskList => {
          this.taskListService.getTaskByTaskListId(taskList.id).subscribe({
            next: (tasks: any[]) => {
              const listToUpdate = this.taskLists.find(list => list.id === taskList.id);
              if (listToUpdate) {
                listToUpdate.tasks = tasks;
              }
            },
            error: (error) => {
              console.error('Error loading tasks:', error);
            },
          });
        });

        this.connectedLists = this.taskLists.map(list => list.name);
      },
      error: (error) => {
        console.error('Error loading task lists:', error);
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
    this.loadTaskLists();
  }


  toggleCreateTaskForm(list?: TaskList): void {
    this.showCreateTaskForm = !this.showCreateTaskForm;
    this.newTaskTitle = '';
    this.newTaskDescription = '';
    this.newTaskDueDate = '';
    this.selectedList = list || null;
  }

  deleteTask(taskId: string): void {
    this.taskListService.deleteTask(taskId);
    this.loadTaskLists();
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
      id: '',
      title: this.newTaskTitle,
      status: 'PENDING',
      description: this.newTaskDescription,
      listId: list.id,
      dueDate: this.newTaskDueDate // Include due date
    };

    this.taskListService.addTask(newTask).subscribe({
      next: (task) => {
        console.log('Task created:', task);
      },
      error: (err) => {
        console.error('Failed to create task:', err);
      }
    });

    this.toggleCreateTaskForm();
    this.loadTaskLists();
    console.log(this.taskLists);
  }

  toggleCreateListForm(): void {
    this.showCreateListForm = !this.showCreateListForm;
    this.newListName = '';
  }

  createTaskList(): void {
    if (!this.newListName.trim()) {
      alert('O nome da lista é obrigatório!');
      return;
    }

    const newTaskList = {
      id: '',
      name: this.newListName,
      boardId: this.boardId!,
      tasks: []
    };

    this.taskListService.createTaskList(newTaskList).subscribe({
      next: (list) => {
        console.log('Task list created:', list);
        this.taskLists.push(list);
      },
      error: (err) => {
        console.error('Failed to create task list:', err);
      }
    });

    this.toggleCreateListForm();
  }

  deleteTaskList(taskListId: string): void {
    this.taskListService.deleteTaskList(taskListId).subscribe({
      next: () => {
        this.taskLists = this.taskLists.filter(list => list.id !== taskListId);
      },
      error: (err) => {
        console.error('Failed to delete task list:', err);
      }
    });
  }

  toggleRenameListForm(list?: TaskList): void {
    this.showRenameListForm = !this.showRenameListForm;
    this.renameListName = list ? list.name : '';
    this.selectedListToRename = list || null;
  }

  renameTaskList(): void {
    if (!this.renameListName.trim()) {
      alert('O nome da lista é obrigatório!');
      return;
    }

    if (this.selectedListToRename) {
      this.selectedListToRename.name = this.renameListName;
      this.taskListService.updateTaskListTitle(this.selectedListToRename.id, this.renameListName).subscribe({
        next: () => {
          console.log('Task list renamed successfully.');
        },
        error: (err) => {
          console.error('Failed to rename task list:', err);
        }
      });
    }

    this.toggleRenameListForm();
  }

  toggleRenameBoardForm(): void {
    this.showRenameBoardForm = !this.showRenameBoardForm;
    this.renameBoardName = this.boardName;
  }

  renameBoard(): void {
    if (!this.renameBoardName.trim()) {
      alert('O nome do board é obrigatório!');
      return;
    }

    if (this.boardId) {
      this.taskListService.updateBoardTitle(this.boardId, this.renameBoardName).subscribe({
        next: () => {
          this.boardName = this.renameBoardName;
          console.log('Board renamed successfully.');
        },
        error: (err) => {
          console.error('Failed to rename board:', err);
        }
      });
    }

    this.toggleRenameBoardForm();
    window.location.reload();

    
  }

  toggleEditTaskForm(task?: Task): void {
    this.showEditTaskForm = !this.showEditTaskForm;
    if (task) {
      this.editTask = { ...task };
      this.selectedCategory = task.status || '';
      this.dueDate = ''; // Set to task's due date if available
    } else {
      this.editTask = null;
      this.selectedCategory = '';
      this.dueDate = '';
    }
  }

  saveTaskChanges(): void {
    if (this.editTask) {
      // debugger;
      this.editTask.status = this.selectedCategory;
      // Add dueDate to the task object if needed
      this.taskListService.updateTaskTitle(this.editTask.id, this.editTask.title).subscribe({
        next: () => {
          console.log('Task updated successfully.');
          this.loadTaskLists();
        },
        error: (err) => {
          console.error('Failed to update task:', err);
        }
      });
      this.toggleEditTaskForm();
    }
  }

  trackByTaskListId(index: number, taskList: TaskList): string {
    return taskList.id;
  }
}