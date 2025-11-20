import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Category } from '../models/category.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private readonly API_URL = `${environment.apiUrl}/category`;

  constructor(private http: HttpClient) {}

  createCategory(name: string): Observable<Category> {
    return this.http.post<Category>(`${this.API_URL}/createCategory/${name}`, {});
  }

  getCategory(categoryId: number): Observable<Category> {
    return this.http.get<Category>(`${this.API_URL}/${categoryId}`);
  }

  getAllCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.API_URL}/getAllCategories`);
  }

  updateCategoryName(category: Category): Observable<Category> {
    return this.http.put<Category>(`${this.API_URL}/updateCategoryName`, category);
  }

  deleteCategory(categoryId: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/deleteCategory/${categoryId}`);
  }
}