import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-main-screen',
  standalone: true,
  imports: [CommonModule, DragDropModule, RouterModule],
  templateUrl: './main-screen.component.html',
  styleUrls: ['./main-screen.component.css']
})
export class MainScreenComponent {
  todo = [
    { title: 'Create social media calendar', description: 'Plan content for next month' },
    { title: 'Design new landing page', description: 'Homepage redesign project' },
    { title: 'Email campaign', description: 'Q1 newsletter' }
  ];

  inProgress = [
    { title: 'Fix navigation bug', description: 'Mobile menu issues' },
    { title: 'API integration', description: 'Connect to payment gateway' }
  ];

  done = [
    { title: 'Icon set update', description: 'Refresh product icons' }
  ];


  drop(event: CdkDragDrop<any[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
    }
  }

  addTask(list: string) {
    const newTask = {
      title: 'Nova Tarefa',
      description: 'Descrição da nova tarefa'
    };

    if (list === 'todo') {
      this.todo.push(newTask);
    } else if (list === 'inProgress') {
      this.inProgress.push(newTask);
    } else if (list === 'done') {
      this.done.push(newTask);
    }
  }
}