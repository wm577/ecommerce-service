import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Category} from '../../../../shared/models/category.model';
import {HttpClient} from '@angular/common/http';
import {CategoryService} from '../../services/category.service';
import {CommonModule} from '@angular/common';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-category',
  imports: [
    FormsModule,
    CommonModule,
    ReactiveFormsModule,
  ],
  templateUrl: './category.component.html',
  styleUrl: './category.component.css',
})
export class CategoryComponent implements OnInit{
  categories$: Observable<Category[]>;
  categoryForm : FormGroup;
  categories : Category[]= [];
  isEditMode: boolean = false;
  selectedCategoryId: number | null = null;

  constructor(private fb:FormBuilder, private categoryService: CategoryService,private cdr: ChangeDetectorRef) {
    this.categoryForm = this.fb.group({
      name: ['', [Validators.required,
                  Validators.minLength(3),
                  Validators.maxLength(50)]],
      description: ['', [Validators.required,
                         Validators.maxLength(255)]],
      });
    this.categories$ = this.categoryService.getAllCategories();
  }

  ngOnInit(){
    this.loadCategories();
  }

  loadCategories() {
    this.categories$ = this.categoryService.getAllCategories();
  }

  onSubmit(){
    if(this.categoryForm.invalid) return;

    const categoryData: Category = this.categoryForm.value;

    if(this.isEditMode && this.selectedCategoryId){
      this.categoryService.updateCategory(this.selectedCategoryId, categoryData).subscribe({
        next: ()=> this.handleSuccess('Updated'),
        error: (err) => console.error(err)
      });
    }else {
      this.categoryService.createCategory(categoryData).subscribe({
        next: ()=> this.handleSuccess('Created'),
        error: (err) => console.error(err)
      });
    }
  }

  private handleSuccess(msg: string): void {
    console.log(`Category ${msg} successfully!`);
    this.resetForm();
    this.loadCategories(); // Refresh the list/dropdown
  }

  editCategory(category: Category){
    this.isEditMode = true;
    this.selectedCategoryId = category.id!;
    this.categoryForm.patchValue({
      name: category.name,
      description: category.description,
    });
  }

  resetForm(){
    this.categoryForm.reset();
    this.isEditMode = false;
    this.selectedCategoryId = null;
  }
}
