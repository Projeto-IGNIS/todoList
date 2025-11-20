import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { NotificationService } from '../services/notification.service';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const notificationService = inject(NotificationService);
  const router = inject(Router);
  const authService = inject(AuthService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = 'Ocorreu um erro. Tente novamente.';
      let showNotification = true;

      if (error.error instanceof ErrorEvent) {
        // Erro do lado do cliente
        errorMessage = `Erro: ${error.error.message}`;
      } else {
        // Erro do lado do servidor
        switch (error.status) {
          case 400:
            errorMessage = 'Requisição inválida. Verifique os dados enviados.';
            break;
          case 401:
            // Apenas mostra mensagem se for erro de autenticação em requisição (não no guard)
            if (req.url.includes('/login') || authService.isAuthenticated()) {
              errorMessage = 'Credenciais inválidas ou sessão expirada.';
            } else {
              showNotification = false; // Não mostra notificação em redirecionamentos do guard
            }
            authService.logout();
            router.navigate(['/login'], { replaceUrl: true });
            break;
          case 404:
            errorMessage = 'Recurso não encontrado.';
            break;
          case 500:
            errorMessage = 'Erro no servidor. Tente novamente mais tarde.';
            break;
          default:
            errorMessage = error.error?.message || error.message || errorMessage;
        }
      }

      if (showNotification) {
        notificationService.showError(errorMessage);
      }
      return throwError(() => error);
    })
  );
};
