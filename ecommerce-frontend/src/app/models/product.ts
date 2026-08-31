export interface Product {

  id: number;

  name: string;

  description: string;

  price: number;

  stockQuantity: number;

  imageUrl: string | null;

  categoryId: number;

  categoryName: string;

}