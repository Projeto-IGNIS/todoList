import { Component, inject } from '@angular/core';
import { SignUpService } from '../services/signUp.service';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common'; 

@Component({
  selector: 'app-cadastro',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './signUp.component.html',
  styleUrls: ['./signUp.component.css']
})
export class CadastroComponent {

    constructor(private readonly router: Router) {}

    private readonly signupService = inject(SignUpService);

    signUpSuccess: boolean = false;
    userName: string = '';
    signUpError: string | null = null;



  signupForm: FormGroup = new FormGroup({
    name: new FormControl(""),
    email: new FormControl(""),
    password: new FormControl("")
  });

  onSignup() {
    const formValues = this.signupForm.value;
    console.log('Sign up form values:', formValues);
    
    this.signupService.signUp(formValues).subscribe({
      next: (data) => {
        console.log(data);
        this.signUpSuccess = true;
        this.userName = formValues.name;
        setTimeout(() => {
          this.router.navigate(['/boards']);
        }, 2000);
      },
      error: (error) => {
        console.error('Erro no cadastro:', error);
        this.signUpError = 'Erro ao criar conta. Tente novamente.';
        setTimeout(() => {
          this.signUpError = null;
        }, 3000);
      },
    });
  }
}
