import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SignUpService } from '../services/signUp.service';
import { User } from '../models/user.model';

@Component({
  selector: 'app-signup',
  templateUrl: './signUp.component.html',
  styleUrls: ['./signUp.component.css']
})
export class SignUpComponent {
  signupForm: FormGroup;
  errorMessage: string = '';

  constructor(
    private readonly fb: FormBuilder,
    private readonly signUpService: SignUpService,
    private readonly router: Router
  ) {
    this.signupForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.signupForm.valid) {
      const user: User = this.signupForm.value;
      this.signUpService.register(user).subscribe({
        next: () => {
          this.router.navigate(['/login']);
        },
        error: (err) => {
          this.errorMessage = err.error?.message || 'Erro ao cadastrar usuário.';
        }
      });
    }
  }
}
