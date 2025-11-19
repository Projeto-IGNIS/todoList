export interface Task {
  id?: number;
  title: string;
  description?: string;
  status: TaskStatus;
  listId: number;
  categoryId?: number;
  dueDate?: Date | string | null;
}

export enum TaskStatus {
  PENDING = 'PENDING',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED'
}

export interface CreateTaskRequest {
  title: string;
  description?: string;
  status: TaskStatus;
  listId: number;
  categoryId?: number;
  dueDate?: string;
}
