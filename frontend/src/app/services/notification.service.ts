import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface Toast {
  message: string;
  type: 'success' | 'error' | 'info' | 'warning';
  duration?: number;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private toastSubject = new BehaviorSubject<Toast | null>(null);
  public toast$ = this.toastSubject.asObservable();

  showSuccess(message: string, duration = 3000): void {
    this.show({ message, type: 'success', duration });
  }

  showError(message: string, duration = 4000): void {
    this.show({ message, type: 'error', duration });
  }

  showInfo(message: string, duration = 3000): void {
    this.show({ message, type: 'info', duration });
  }

  showWarning(message: string, duration = 3000): void {
    this.show({ message, type: 'warning', duration });
  }

  private show(toast: Toast): void {
    this.toastSubject.next(toast);
    
    setTimeout(() => {
      this.toastSubject.next(null);
    }, toast.duration);
  }
}
