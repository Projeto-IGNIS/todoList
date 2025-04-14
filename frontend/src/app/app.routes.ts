import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { MainScreenComponent } from './main-screen/main-screen.component';
import { BoardsComponent } from './boards/boards.component';
import { CadastroComponent } from './signup/signUp.component';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'main', component: MainScreenComponent },
  { path: 'boards', component: BoardsComponent },
  { path: 'signup', component: CadastroComponent }
];
