package com.ecommerce.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ecommerce.backend.dto.CategoryRequestDTO;
import com.ecommerce.backend.dto.CategoryResponseDTO;
import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Get all categories
    public List<CategoryResponseDTO> getAllCategories() {

        return categoryRepository.findAll()
                .stream()							//A Stream is like a pipeline that lets you process data stepbystep (onebyone)
                .map(this::convertToResponseDTO)	//map() takes the value from category and map it to CategoryResponseDTO. [convertToResponseDTO] For each Category, call the method convertToResponseDTO(category)
                .collect(Collectors.toList());		//This takes the transformed stream and turns it back into a List.
    }

    // Get category by ID
    public CategoryResponseDTO getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id));

        return convertToResponseDTO(category);
    }

    // Create category
    public CategoryResponseDTO createCategory(
            CategoryRequestDTO request) {

        Category category = new Category();

        category.setName(request.getName());

        Category savedCategory =
                categoryRepository.save(category);

        return convertToResponseDTO(savedCategory);
    }

    // Update category
    public CategoryResponseDTO updateCategory(
            Long id,
            CategoryRequestDTO request) {

        Category existingCategory =
                categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found with id: "
                                        + id));

        existingCategory.setName(request.getName());

        Category updatedCategory =
                categoryRepository.save(existingCategory);

        return convertToResponseDTO(updatedCategory);
    }

    // Delete category
    public void deleteCategory(Long id) {

        Category existingCategory =
                categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found with id: "
                                        + id));

        categoryRepository.delete(existingCategory);
    }

    // Entity → Response DTO
    private CategoryResponseDTO convertToResponseDTO(
            Category category) {

        return new CategoryResponseDTO(
                category.getId(),
                category.getName()
        );
    }
}