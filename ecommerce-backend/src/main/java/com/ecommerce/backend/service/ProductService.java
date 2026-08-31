package com.ecommerce.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ecommerce.backend.dto.ProductRequestDTO;
import com.ecommerce.backend.dto.ProductResponseDTO;
import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.repository.CategoryRepository;
import com.ecommerce.backend.repository.ProductRepository;

import com.ecommerce.backend.exception.ResourceNotFoundException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // GET all products
    public List<ProductResponseDTO> getAllProducts() {

        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    // GET products by category
    public List<ProductResponseDTO> getProductsByCategory(Long categoryId) {

        List<Product> products =
                productRepository.findByCategoryId(categoryId);

        return products.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // GET product by ID
    public ProductResponseDTO getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id));

        return convertToResponseDTO(product);
    }

    // CREATE product
    public ProductResponseDTO createProduct(ProductRequestDTO request) {

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: "
                                + request.getCategoryId()));

        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return convertToResponseDTO(savedProduct);
    }

    // UPDATE product
    public ProductResponseDTO updateProduct(
            Long id,
            ProductRequestDTO request) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id));

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: "
                                + request.getCategoryId()));

        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setStockQuantity(request.getStockQuantity());
        existingProduct.setImageUrl(request.getImageUrl());
        existingProduct.setCategory(category);

        Product updatedProduct =
                productRepository.save(existingProduct);

        return convertToResponseDTO(updatedProduct);
    }

    // DELETE product
    public void deleteProduct(Long id) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id));

        productRepository.delete(existingProduct);
    }

    // Convert Entity → Response DTO
    private ProductResponseDTO convertToResponseDTO(Product product) {

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }
}