import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { BoardService } from '../services/board.service';
import { AuthInterceptor } from '../interceptors/auth.interceptor';

interface Board {
  id: string;
  ownerId: string;
  title: string;
  description?: string;
}

@Component({
  selector: 'app-boards',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  providers: [
    {
      provide: 'HTTP_INTERCEPTORS',
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  templateUrl: './boards.component.html',
  styleUrls: ['./boards.component.css']
})
export class BoardsComponent implements OnInit {
  boards: Array<Board> = [];
  showCreateForm: boolean = false;
  newBoardTitle: string = '';
  newBoardDescription: string = '';

  constructor(private readonly boardService: BoardService) {}

  ngOnInit(): void {
    this.loadBoards();
  }

  loadBoards(): void {
    this.boardService.getAllBoards().subscribe({
      next: (data: Array<Board>) => {
        this.boards = data;
        console.log('Boards loaded:', this.boards);
      },
      error: (err) => {
        console.error('Failed to load boards:', err);
      }
    });

  }

  deleteBoard(id: string) {
    this.boardService.deleteBoard(id).subscribe({
      next: () => {
        console.log('Board deleted successfully');
      },
      error: (err) => {
        console.error('Failed to delete board:', err);
      }
    })

    this.boards = this.boards.filter(board => board.id !== id);
    window.location.reload();

    console.log('Board deleted:', id);
  }

  toggleCreateForm(): void {
    this.showCreateForm = !this.showCreateForm;
    this.newBoardTitle = '';
    this.newBoardDescription = '';
  }

  createBoard(): void {
    const ownerId = this.boardService.getOwnerId();
    if (!this.newBoardTitle.trim()) {
      alert('O título é obrigatório!');
      return;
    }

    const newBoard: Board = {
      id: "",
      ownerId: ownerId ?? '',
      title: this.newBoardTitle,
      description: this.newBoardDescription
    };

    this.boards.push(newBoard);
    this.boardService.createBoard(newBoard).subscribe({
      next: (data: Board) => {
        console.log('Board created:', data);
      },
      error: (err) => {
        console.error('Failed to create board:', err);
      }
    })
    
    this.toggleCreateForm();
    window.location.reload();

  }
}
