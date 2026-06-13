import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {Product} from '../../../../shared/models/product.model';
import {CategoryService} from '../../services/category.service';
import {ProductService} from '../../services/product.service';
import {Category} from '../../../../shared/models/category.model';
import { inject } from '@angular/core'; // Add 'inject' here

@Component({
  selector: 'app-product',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './product.component.html',
  styleUrl: './product.component.css',
})
export class ProductComponent implements OnInit{
  productForm! : FormGroup;
  products: Product[] = [];
  categories: Category[] = [];
  selectedFile: File | null = null;
  isEditMode = false;
  currentProductId: number | null = null;
  private cdr = inject(ChangeDetectorRef);

  private fb = inject(FormBuilder);
  private productService = inject(ProductService);
  private categoryService = inject(CategoryService);

  constructor() {}

  ngOnInit(): void {
    this.initForm();
    this.categoryService.getAllCategories().subscribe(cats => {
      this.categories = cats;
    this.loadProducts(); });
  }

  initForm(){
    this.productForm = this.fb.group({
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      price: [null, [Validators.required, Validators.min(1)]],
      quantity: [null, [Validators.required, Validators.min(0)]],
      isActive: [false],
      categoryId: [null, [Validators.required]]
    });
  }

  loadProducts() {
    this.productService.getAllProductsForAdmin().subscribe({
      next: (res) => {
        const mappedData = res.map(prod => {
          const category = this.categories.find(c => c.id === prod.categoryId);
          return {
            ...prod,
            categoryName: category ? category.name : 'No Category'
          };
        });
        this.products = [...mappedData];
        this.cdr.detectChanges();
        console.log('UI should refresh now with:', this.products.length, 'items');
      }
    });
  }

  loadCategories(){
    this.categoryService.getAllCategories()
                        .subscribe(res => this.categories = res);
  }

  onSubmit() {
    if (this.productForm.invalid) return;

    const existingProduct = this.products.find(p => p.id === this.currentProductId);

    const productData = {
      ...this.productForm.value,
      byteImg: this.selectedFile ? this.selectedFile : existingProduct?.byteImg
    };

    if(this.isEditMode && this.currentProductId){
      this.productService.updateProduct(this.currentProductId, productData).subscribe(() =>{
        console.log('Product updated successfully');
        // this.loadProducts();
        this.resetForm();
          setTimeout(() => {
            this.loadProducts();
          }, 200);
        });
    }else {
      this.productService.addNewProduct(productData).subscribe(() =>{
        this.resetForm();
        this.loadProducts();
      });
    }
  }

  trackById(index: number, item: any): number {
    return item.id;
  }

  getCurrentImage() {
    const prod = this.products.find(p => p.id === this.currentProductId);
    return prod ? prod.byteImg : null;
  }

  onEdit(product: Product) {
    this.isEditMode = true;
    this.currentProductId = product.id ?? null;

    this.productForm.patchValue({
      name: product.name,
      description: product.description,
      price: product.price,
      quantity: product.quantity,
      isActive : product.isActive,
      categoryId: product.categoryId
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
    document.getElementById('productName')?.focus();
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      // Limit: 1MB (1024 * 1024 bytes)
      const maxSize = 1 * 1024 * 1024;

      if (file.size > maxSize) {
        alert("File is too large! Please select an image under 1MB.");
        event.target.value = ''; // Reset the input
        this.selectedFile = null;
      } else {
        this.selectedFile = file;
      }
    }
  }

  onAddStock(productId: number): void {
    const amount = prompt("Enter quantity to add:");
    if(amount && !isNaN(+amount)){
      this.productService.addStock(productId, +amount).subscribe(() => this.loadProducts());
    }
  }

  onDelete(productId: number){
    if(confirm("Are you sure to delete this product?")){
      this.productService.deleteProduct(productId).subscribe(()=>this.loadProducts());
    }
  }

  resetForm() {
    this.productForm.reset();
    this.isEditMode = false;
    this.selectedFile = null;

    this.productForm.reset({
      name:null,
      description:null,
      price: 0,      // Optional: set default values
      quantity: 0,
      isActive: true,
      categoryId: null
    });
    const fileInput = document.querySelector('input[type="file"]') as HTMLInputElement;
    if (fileInput) fileInput.value = '';
  }
}
