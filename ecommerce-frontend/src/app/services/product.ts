import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Product as ProductModel } from '../models/product';

@Injectable({
  providedIn: 'root'
})
export class Product {

  private apiUrl = 'http://localhost:8080/api/products';

  constructor(private http: HttpClient) {
  }

  getProducts(): Observable<ProductModel[]> {

    return this.http.get<ProductModel[]>(
      this.apiUrl
    );

  }

  getProductsByCategory(categoryId: number): Observable<ProductModel[]> {

    return this.http.get<ProductModel[]>(
      `${this.apiUrl}/category/${categoryId}`
    );

  }

  getProduct(id: number): Observable<ProductModel> {

    return this.http.get<ProductModel>(
      `${this.apiUrl}/${id}`
    );

  }

  createProduct(product: ProductModel): Observable<ProductModel> {

    return this.http.post<ProductModel>(
      this.apiUrl,
      product
    );

  }

  updateProduct(
    id: number,
    product: ProductModel
  ): Observable<ProductModel> {

    return this.http.put<ProductModel>(
      `${this.apiUrl}/${id}`,
      product
    );

  }

  deleteProduct(id: number): Observable<void> {

    return this.http.delete<void>(
      `${this.apiUrl}/${id}`
    );

  }

}