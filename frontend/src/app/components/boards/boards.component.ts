import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { BoardService } from '../../services/board.service';
import { AuthService } from '../../services/auth.service';
import { NotificationService } from '../../services/notification.service';
import { Board } from '../../models/board.model';
import { TaskListViewComponent } from '../task-list-view/task-list-view.component';

@Component({
  selector: 'app-boards',
  standalone: true,
  imports: [CommonModule, FormsModule, TaskListViewComponent],
  templateUrl: './boards.component.html',
  styleUrls: ['./boards.component.css']
})
export class BoardsComponent implements OnInit {
  boards: Board[] = [];
  isLoading = false;
  showCreateModal = false;
  showSidebar = true;
  newBoardTitle = '';
  selectedBoard: Board | null = null;

  constructor(
    private readonly boardService: BoardService,
    private readonly authService: AuthService,
    private readonly notificationService: NotificationService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.loadBoards();
  }

  loadBoards(): void {
    this.isLoading = true;
    const userId = this.authService.getUserId();
    
    if (!userId) {
      this.router.navigate(['/login']);
      return;
    }

    this.boardService.getMyBoards(Number(userId)).subscribe({
      next: (boards) => {
        this.boards = boards;
        this.isLoading = false;
        
        // Selecionar o primeiro board automaticamente
        if (this.boards.length > 0 && !this.selectedBoard) {
          this.selectBoard(this.boards[0]);
        }
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  selectBoard(board: Board): void {
    this.selectedBoard = board;
  }

  openCreateModal(): void {
    this.showCreateModal = true;
    this.newBoardTitle = '';
  }

  closeCreateModal(): void {
    this.showCreateModal = false;
    this.newBoardTitle = '';
  }

  createBoard(): void {
    if (!this.newBoardTitle.trim()) {
      this.notificationService.showWarning('Digite um título para o board');
      return;
    }

    const userId = this.authService.getUserId();
    if (!userId) return;

    this.isLoading = true;

    this.boardService.createBoard({
      title: this.newBoardTitle,
      ownerId: Number(userId)
    }).subscribe({
      next: (board) => {
        this.boards.push(board);
        this.notificationService.showSuccess('Board criado com sucesso!');
        this.closeCreateModal();
        this.selectBoard(board);
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  toggleFavorite(board: Board, event: Event): void {
    event.stopPropagation();
    
    if (!board.id) return;

    this.boardService.toggleFavorite(board.id).subscribe({
      next: () => {
        board.favorite = !board.favorite;
        this.notificationService.showSuccess(
          board.favorite ? 'Adicionado aos favoritos' : 'Removido dos favoritos'
        );
      }
    });
  }

  deleteBoard(board: Board, event: Event): void {
    event.stopPropagation();
    
    if (!board.id) return;
    
    if (!confirm(`Deseja realmente excluir o board "${board.title}"?`)) {
      return;
    }

    this.boardService.deleteBoard(board.id).subscribe({
      next: () => {
        this.boards = this.boards.filter(b => b.id !== board.id);
        this.notificationService.showSuccess('Board excluído com sucesso!');
        
        if (this.selectedBoard?.id === board.id) {
          this.selectedBoard = this.boards.length > 0 ? this.boards[0] : null;
        }
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  toggleSidebar(): void {
    this.showSidebar = !this.showSidebar;
  }
}
