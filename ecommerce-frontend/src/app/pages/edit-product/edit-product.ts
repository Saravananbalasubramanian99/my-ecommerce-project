import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { Product } from '../../services/product';
import { Product as ProductModel } from '../../models/product';

@Component({
  selector: 'app-edit-product',

  imports: [
    FormsModule
  ],

  templateUrl: './edit-product.html',

  styleUrl: './edit-product.css'
})
export class EditProduct implements OnInit {

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

  saving = signal(false);

  successMessage = signal('');

  errorMessage = signal('');

  productId = 0;

  constructor(
    private productService: Product,
    private route: ActivatedRoute,
    private router: Router
  ) {
  }

  ngOnInit(): void {

    this.productId = Number(
      this.route.snapshot.paramMap.get('id')
    );

    this.loadProduct();

  }

  loadProduct(): void {

    this.loading.set(true);

    this.productService
      .getProduct(this.productId)
      .subscribe({

        next: (response) => {

          console.log(
            'PRODUCT LOADED:',
            response
          );

          this.product.set(response);

          this.loading.set(false);

        },

        error: (error) => {

          console.error(
            'FAILED TO LOAD PRODUCT'
          );

          console.error(error);

          this.loading.set(false);

          this.errorMessage.set(
            'Unable to load product.'
          );

        }

      });

  }

  updateProduct(): void {

    this.successMessage.set('');

    this.errorMessage.set('');

    this.saving.set(true);

    this.productService
      .updateProduct(
        this.productId,
        this.product()
      )
      .subscribe({

        next: (response) => {

          console.log(
            'PRODUCT UPDATED:',
            response
          );

          this.product.set(response);

          this.saving.set(false);

          this.successMessage.set(
            'Product updated successfully!'
          );

        },

        error: (error) => {

          console.error(
            'FAILED TO UPDATE PRODUCT'
          );

          console.error(error);

          this.saving.set(false);

          this.errorMessage.set(
            error.error?.message ||
            'Unable to update product. Please try again.'
          );

        }

      });

  }

  cancel(): void {

    this.router.navigate(['/electronics']);

  }

}