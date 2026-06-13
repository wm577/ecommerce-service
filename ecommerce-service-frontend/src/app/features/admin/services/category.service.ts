import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {API_ENDPOINTS} from '../../../core/Config';
import {Observable} from 'rxjs';
import {Category} from '../../../shared/models/category.model';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {

  private readonly ADMIN_URL = API_ENDPOINTS.adminCategory;
  constructor(private http: HttpClient) {}

  getAllCategories() : Observable<Category[]>{
    return this.http.get<Category[]>(this.ADMIN_URL);
  }

  createCategory(categoryDto: Category) : Observable<Category>{
    return this.http.post<Category>(this.ADMIN_URL, categoryDto);
  }

  updateCategory(id:number, categoryDto: Category) : Observable<Category>{
    return this.http.put<Category>(`${this.ADMIN_URL}/${id}`, categoryDto);
  }
}
