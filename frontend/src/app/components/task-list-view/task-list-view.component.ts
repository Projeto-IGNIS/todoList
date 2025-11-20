import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CdkDragDrop, DragDropModule, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Board } from '../../models/board.model';
import { TaskList } from '../../models/task-list.model';
import { Task, TaskStatus } from '../../models/task.model';
import { BoardService } from '../../services/board.service';
import { TaskListService } from '../../services/task-list.service';
import { TaskService } from '../../services/task.service';
import { NotificationService } from '../../services/notification.service';

@Component({
  selector: 'app-task-list-view',
  standalone: true,
  imports: [CommonModule, FormsModule, DragDropModule],
  templateUrl: './task-list-view.component.html',
  styleUrls: ['./task-list-view.component.css']
})
export class TaskListViewComponent implements OnChanges {
  @Input() board!: Board;

  taskLists: TaskList[] = [];
  tasks: { [key: number]: Task[] } = {};
  isLoading = false;

  showCreateListModal = false;
  showCreateTaskModal = false;
  showEditTaskModal = false;
  showEditListModal = false;
  
  newListTitle = '';
  selectedListId: number | null = null;
  
  newTask: Task = this.getEmptyTask();
  editingTask: Task = this.getEmptyTask();
  originalTask: Task = this.getEmptyTask();
  editingList: TaskList = this.getEmptyList();
  
  searchQuery = '';
  filterStatus: 'ALL' | TaskStatus = 'ALL';
  sortBy: 'title' | 'dueDate' | 'status' = 'dueDate';

  TaskStatus = TaskStatus;

  constructor(
    private readonly boardService: BoardService,
    private readonly taskListService: TaskListService,
    private readonly taskService: TaskService,
    private readonly notificationService: NotificationService
  ) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['board'] && this.board) {
      this.loadTaskLists();
    }
  }

  loadTaskLists(): void {
    if (!this.board.id) return;

    this.isLoading = true;
    this.boardService.getTaskListsByBoard(this.board.id).subscribe({
      next: (lists) => {
        this.taskLists = lists;
        this.loadAllTasks();
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  loadAllTasks(): void {
    this.taskService.getAllTasks().subscribe({
      next: (allTasks) => {
        // Organizar tasks por listId
        this.tasks = {};
        this.taskLists.forEach(list => {
          if (list.id) {
            this.tasks[list.id] = allTasks.filter(task => task.listId === list.id);
          }
        });
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  // Task List operations
  openCreateListModal(): void {
    this.showCreateListModal = true;
    this.newListTitle = '';
  }

  closeCreateListModal(): void {
    this.showCreateListModal = false;
    this.newListTitle = '';
  }

  createTaskList(): void {
    if (!this.newListTitle.trim() || !this.board.id) {
      this.notificationService.showWarning('Digite um título para a lista');
      return;
    }

    const newList: TaskList = {
      title: this.newListTitle,
      boardId: this.board.id
    };

    this.taskListService.createTaskList(newList).subscribe({
      next: () => {
        this.notificationService.showSuccess('Lista criada com sucesso!');
        this.closeCreateListModal();
        this.loadTaskLists();
      }
    });
  }

  openEditListModal(list: TaskList): void {
    this.editingList = { ...list };
    this.showEditListModal = true;
  }

  closeEditListModal(): void {
    this.showEditListModal = false;
    this.editingList = this.getEmptyList();
  }

  updateTaskList(): void {
    if (!this.editingList.title.trim()) {
      this.notificationService.showWarning('Digite um título para a lista');
      return;
    }

    this.taskListService.updateTaskList(this.editingList).subscribe({
      next: () => {
        this.notificationService.showSuccess('Lista atualizada com sucesso!');
        this.closeEditListModal();
        this.loadTaskLists();
      }
    });
  }

  deleteTaskList(listId: number): void {
    const list = this.taskLists.find(l => l.id === listId);
    if (!confirm(`Deseja realmente excluir a lista "${list?.title}"?`)) {
      return;
    }

    this.taskListService.deleteTaskList(listId).subscribe({
      next: () => {
        this.notificationService.showSuccess('Lista excluída com sucesso!');
        this.loadTaskLists();
      }
    });
  }

  // Task operations
  openCreateTaskModal(listId: number): void {
    this.selectedListId = listId;
    this.newTask = this.getEmptyTask();
    this.newTask.listId = listId;
    this.showCreateTaskModal = true;
  }

  closeCreateTaskModal(): void {
    this.showCreateTaskModal = false;
    this.newTask = this.getEmptyTask();
    this.selectedListId = null;
  }

  createTask(): void {
    if (!this.newTask.title.trim()) {
      this.notificationService.showWarning('Digite um título para a tarefa');
      return;
    }

    const taskToCreate = {
      title: this.newTask.title,
      description: this.newTask.description,
      status: this.newTask.status,
      listId: this.newTask.listId,
      dueDate: this.newTask.dueDate ? new Date(this.newTask.dueDate).toISOString() : undefined
    };

    this.taskService.createTask(taskToCreate).subscribe({
      next: () => {
        this.notificationService.showSuccess('Tarefa criada com sucesso!');
        this.closeCreateTaskModal();
        this.loadAllTasks();
      }
    });
  }

  openEditTaskModal(task: Task): void {
    this.originalTask = { ...task };
    this.editingTask = { ...task };
    // Converter data para formato compatível com input date
    if (this.editingTask.dueDate) {
      const date = new Date(this.editingTask.dueDate);
      this.editingTask.dueDate = date.toISOString().split('T')[0];
    }
    this.showEditTaskModal = true;
  }

  closeEditTaskModal(): void {
    this.showEditTaskModal = false;
    this.editingTask = this.getEmptyTask();
  }

  updateTask(): void {
    if (!this.editingTask.title.trim()) {
      this.notificationService.showWarning('Digite um título para a tarefa');
      return;
    }

    // Convert dueDate back to Date if it's a string
    if (typeof this.editingTask.dueDate === 'string') {
      this.editingTask.dueDate = this.editingTask.dueDate ? new Date(this.editingTask.dueDate) : null;
    }

    this.taskService.updateTaskTitle(this.editingTask).subscribe({
      next: () => {
        // Check if status changed
        if (this.editingTask.status !== this.originalTask.status) {
          this.taskService.updateTaskStatus(this.editingTask).subscribe({
            next: () => {
              this.notificationService.showSuccess('Tarefa atualizada com sucesso!');
              this.closeEditTaskModal();
              this.loadAllTasks();
            },
            error: (err) => {
              console.error('Erro ao atualizar status:', err);
              this.notificationService.showError('Erro ao atualizar status da tarefa');
            }
          });
        } else {
          this.notificationService.showSuccess('Tarefa atualizada com sucesso!');
          this.closeEditTaskModal();
          this.loadAllTasks();
        }
      },
      error: (err) => {
        console.error('Erro ao atualizar tarefa:', err);
        this.notificationService.showError('Erro ao atualizar tarefa');
      }
    });
  }

  toggleTaskStatus(task: Task): void {
    const updatedTask = { ...task };
    updatedTask.status = task.status === TaskStatus.COMPLETED ? TaskStatus.PENDING : TaskStatus.COMPLETED;

    this.taskService.updateTaskStatus(updatedTask).subscribe({
      next: () => {
        this.loadAllTasks();
      }
    });
  }

  deleteTask(taskId: number): void {
    if (!confirm('Deseja realmente excluir esta tarefa?')) {
      return;
    }

    this.taskService.deleteTask(taskId).subscribe({
      next: () => {
        this.notificationService.showSuccess('Tarefa excluída com sucesso!');
        this.loadAllTasks();
      }
    });
  }

  drop(event: CdkDragDrop<Task[]>): void {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      const task = event.previousContainer.data[event.previousIndex];
      const newListId = parseInt(event.container.id.replace('cdk-drop-list-', ''));

      // Otimistic update: move the task in UI first
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex,
      );

      // Then update the backend
      this.taskService.updateTaskList(task.id!, newListId).subscribe({
        next: () => {
          this.notificationService.showSuccess('Tarefa movida com sucesso!');
        },
        error: () => {
          // Revert the UI change if API call fails
          transferArrayItem(
            event.container.data,
            event.previousContainer.data,
            event.currentIndex,
            event.previousIndex,
          );
          this.notificationService.showError('Erro ao mover tarefa');
        }
      });
    }
  }

  getConnectedLists(): string[] {
    return this.taskLists.map(list => 'cdk-drop-list-' + list.id);
  }

  // Filtering and sorting
  getFilteredTasks(listId: number): Task[] {
    let filteredTasks = this.tasks[listId] || [];

    // Aplicar busca
    if (this.searchQuery.trim()) {
      const query = this.searchQuery.toLowerCase();
      filteredTasks = filteredTasks.filter(task =>
        task.title.toLowerCase().includes(query) ||
        task.description?.toLowerCase().includes(query)
      );
    }

    // Aplicar filtro de status
    if (this.filterStatus !== 'ALL') {
      filteredTasks = filteredTasks.filter(task => task.status === this.filterStatus);
    }

    // Aplicar ordenação
    return this.sortTasks(filteredTasks);
  }

  sortTasks(tasks: Task[]): Task[] {
    return [...tasks].sort((a, b) => {
      switch (this.sortBy) {
        case 'title':
          return a.title.localeCompare(b.title);
        case 'dueDate':
          if (!a.dueDate && !b.dueDate) return 0;
          if (!a.dueDate) return 1;
          if (!b.dueDate) return -1;
          return new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime();
        case 'status':
          return a.status.localeCompare(b.status);
        default:
          return 0;
      }
    });
  }

  hasActiveFilters(): boolean {
    return this.searchQuery.trim() !== '' || this.filterStatus !== 'ALL';
  }

  getTaskCounters(listId: number) {
    const allTasks = this.tasks[listId] || [];
    return {
      total: allTasks.length,
      completed: allTasks.filter(t => t.status === TaskStatus.COMPLETED).length,
      pending: allTasks.filter(t => t.status === TaskStatus.PENDING).length
    };
  }

  isOverdue(task: Task): boolean {
    if (!task.dueDate) return false;
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const dueDate = new Date(task.dueDate);
    dueDate.setHours(0, 0, 0, 0);
    return dueDate < today && task.status !== TaskStatus.COMPLETED;
  }

  formatDate(date: string | Date | null | undefined): string {
    if (!date) return '';
    const d = new Date(date);
    return d.toLocaleDateString('pt-BR');
  }

  toggleFavorite(event: Event): void {
    event.stopPropagation();
    
    if (!this.board.id) return;

    this.boardService.toggleFavorite(this.board.id).subscribe({
      next: () => {
        this.board.favorite = !this.board.favorite;
        this.notificationService.showSuccess(
          this.board.favorite ? 'Adicionado aos favoritos' : 'Removido dos favoritos'
        );
      }
    });
  }

  private getEmptyTask(): Task {
    return {
      title: '',
      description: '',
      status: TaskStatus.PENDING,
      listId: 0,
      dueDate: null
    };
  }

  private getEmptyList(): TaskList {
    return {
      title: '',
      boardId: 0
    };
  }
}
