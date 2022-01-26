
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.prod';
import { Category } from '../interface/category';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private serverURL = environment.serverURL;

  constructor(private http: HttpClient) { }

  public getListCategory(): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.serverURL}/api/category`);
  }

  public addCategory(category: Category): Observable<void> {
    return this.http.post<void>(`${this.serverURL}/api/category/add`,category);
  }

  public deleteCategory(categoryId: number): Observable<void> {
    return this.http.delete<void>(`${this.serverURL}/api/category/delete/categoryId/${categoryId}`);
  }

  public updateCategory(category: Category): Observable<void> {
    return this.http.put<void>(`${this.serverURL}/api/category/update`,category);
  }
}
