import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

import { Product } from '../../services/product';
import { Auth } from '../../services/auth';

import { Product as ProductModel } from '../../models/product';

@Component({
  selector: 'app-electronics',

  imports: [RouterLink],

  templateUrl: './electronics.html',

  styleUrl: './electronics.css'
})
export class Electronics implements OnInit {

  products = signal<ProductModel[]>([]);

  loading = signal(false);

  errorMessage = signal('');

  constructor(
    private productService: Product,
    private authService: Auth
  ) {
  }

  isAdmin(): boolean {

    return this.authService.isAdmin();

  }
  
  ngOnInit(): void {

    this.loadProducts();

  }

  loadProducts(): void {

    this.loading.set(true);

    this.productService
      .getProductsByCategory(4)
      .subscribe({

        next: (response) => {

          console.log(
            'Electronics products loaded:',
            response
          );

          this.products.set(response);

          this.loading.set(false);

        },

        error: (error) => {

          console.error(
            'FAILED TO LOAD PRODUCTS'
          );

          console.error(error);

          this.errorMessage.set(
            'Unable to load products. Please try again.'
          );

          this.loading.set(false);

        }

      });

  }

  deleteProduct(id: number): void {

  const confirmed = confirm(
    'Are you sure you want to delete this product?'
  );

  if (!confirmed) {
    return;
  }

  this.productService
    .deleteProduct(id)
    .subscribe({

      next: () => {

        console.log(
          'PRODUCT DELETED:',
          id
        );

        this.products.update(products =>
          products.filter(product => product.id !== id)
        );

      },

      error: (error) => {

        console.error(
          'FAILED TO DELETE PRODUCT'
        );

        console.error(error);

        this.errorMessage.set(
          error.error?.message ||
          'Unable to delete product. Please try again.'
        );

      }

    });

}

}