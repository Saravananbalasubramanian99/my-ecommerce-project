import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { Product } from '../../services/product';
import { Product as ProductModel } from '../../models/product';

@Component({
  selector: 'app-create-product',

  imports: [
    FormsModule
  ],

  templateUrl: './create-product.html',

  styleUrl: './create-product.css'
})
export class CreateProduct {

  product = signal<ProductModel>({
    id: 0,
    name: '',
    description: '',
    price: 0,
    stockQuantity: 0,
    imageUrl: '',
    categoryId: 4,
    categoryName: ''
  });

  loading = signal(false);

  successMessage = signal('');

  errorMessage = signal('');

  constructor(
    private productService: Product,
    private router: Router
  ) {
  }

  createProduct(): void {

    this.successMessage.set('');
    this.errorMessage.set('');

    this.loading.set(true);

    const productData = this.product();

    this.productService
      .createProduct(productData)
      .subscribe({

        next: (response) => {

          console.log(
            'PRODUCT CREATED:',
            response
          );

          this.loading.set(false);

          this.successMessage.set(
            'Product created successfully!'
          );

          this.product.set({
            id: 0,
            name: '',
            description: '',
            price: 0,
            stockQuantity: 0,
            imageUrl: '',
            categoryId: 4,
            categoryName: ''
          });

        },

        error: (error) => {

          console.error(
            'FAILED TO CREATE PRODUCT'
          );

          console.error(error);

          this.loading.set(false);

          this.errorMessage.set(
            error.error?.message ||
            'Unable to create product. Please try again.'
          );

        }

      });

  }

  cancel(): void {

    this.router.navigate(['/electronics']);

  }

}