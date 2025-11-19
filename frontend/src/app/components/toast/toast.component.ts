import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService, Toast } from '../../services/notification.service';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.css']
})
export class ToastComponent implements OnInit {
  toast: Toast | null = null;

  constructor(private readonly notificationService: NotificationService) {}

  ngOnInit(): void {
    this.notificationService.toast$.subscribe(toast => {
      this.toast = toast;
    });
  }

  close(): void {
    this.toast = null;
  }
}
