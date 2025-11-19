import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { NotificationService } from '../../services/notification.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {
  user: User = {
    name: '',
    email: '',
    password: ''
  };
  confirmPassword = '';
  isLoading = false;

  constructor(
    private readonly authService: AuthService,
    private readonly notificationService: NotificationService,
    private readonly router: Router
  ) {}

  onSubmit(): void {
    if (!this.user.name || !this.user.email || !this.user.password || !this.confirmPassword) {
      this.notificationService.showWarning('Preencha todos os campos');
      return;
    }

    if (this.user.password !== this.confirmPassword) {
      this.notificationService.showError('As senhas não coincidem');
      return;
    }

    if (this.user.password.length < 6) {
      this.notificationService.showWarning('A senha deve ter no mínimo 6 caracteres');
      return;
    }

    this.isLoading = true;

    this.authService.register(this.user).subscribe({
      next: () => {
        this.notificationService.showSuccess('Cadastro realizado com sucesso!');
        this.router.navigate(['/login']);
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }
}
