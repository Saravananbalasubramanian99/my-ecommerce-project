package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.dto.CategoryRequestDTO;
import com.ecommerce.backend.dto.CategoryResponseDTO;
import com.ecommerce.backend.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // GET all categories
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>>
            getAllCategories() {

        return ResponseEntity.ok(
                categoryService.getAllCategories()
        );
    }

    // GET category by ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO>
            getCategoryById(@PathVariable Long id) {

        return ResponseEntity.ok(
                categoryService.getCategoryById(id)
        );
    }

    // CREATE category
    @PostMapping
    public ResponseEntity<CategoryResponseDTO>
            createCategory(@Valid @RequestBody CategoryRequestDTO request) {

        CategoryResponseDTO category =
                categoryService.createCategory(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(category);
    }

    // UPDATE category
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO>
            updateCategory(
                    @PathVariable Long id,
                    @Valid @RequestBody CategoryRequestDTO request) {

        CategoryResponseDTO category =
                categoryService.updateCategory(id, request);

        return ResponseEntity.ok(category);
    }

    // DELETE category
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
            deleteCategory(@PathVariable Long id) {

        categoryService.deleteCategory(id);

        return ResponseEntity.noContent().build();
    }
}