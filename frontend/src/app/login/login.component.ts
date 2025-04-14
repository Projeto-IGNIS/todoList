import { Component, inject } from '@angular/core';
import { LoginService } from '../services/login.service';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import {MatProgressBarModule} from '@angular/material/progress-bar';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, MatProgressBarModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  constructor(private readonly router: Router) {}

  name = 'Login';
  private readonly loginService = inject(LoginService);
  loginError: string | null = null;
  loginSuccess: boolean = false;
  userName: string = '';

  loginForm: FormGroup = new FormGroup({
    email: new FormControl(""),
    password: new FormControl(""),

  });

  onLogin() {
    this.loginService.clearLocalStorage();
    const formValues = this.loginForm.value;
    console.log('Valores do formulário:', formValues);

    this.loginService.login(formValues).subscribe({
      next: (data: string) => {
        const token = data;
        this.loginService.saveToken(token);
        this.userName = formValues.email.split('@')[0];
        this.loginSuccess = true;
        setTimeout(() => {
          this.router.navigate(['/boards']);
        }, 2000);
      },
      error: (error) => { 
        console.error('Erro no login:', error);
        this.loginError = 'Email ou senha incorreto';
        setTimeout(() => {
          this.loginError = null;
        }, 3000);
      },
    });

    this.loginService.getOwnerId(formValues.email).subscribe({next: (ownerId: string) => {
      this.loginService.saveOwnerId(ownerId);
    }})

    
  }
  
  getAllUsers() {
    this.loginService.getAllUsers().subscribe(data => {
      console.log(data);
    });
  }

}