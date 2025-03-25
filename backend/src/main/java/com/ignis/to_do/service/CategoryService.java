package com.ignis.to_do.service;

import org.springframework.stereotype.Service;
import com.ignis.to_do.exception.category_exception.CategoryNotFoundException;
import com.ignis.to_do.model.Category;
import com.ignis.to_do.repository.CategoryRepository;

import jakarta.transaction.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private static final String CATEGORY_NOT_FOUND = "Categoria com ID %s nao encontrada";

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category category) {
        
        return categoryRepository.findByName(category.getName())
                .orElseGet(() -> categoryRepository.save(category));
    }

    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }   
    
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND.formatted(categoryId)));
    }

    public void verifiyIfCategoryExists(Long categoryId) {
        categoryRepository.findById(categoryId).orElseThrow(() -> 
        new CategoryNotFoundException(CATEGORY_NOT_FOUND.formatted(categoryId)));
    }

    public void deleteCategoryById(Long categoryId) {
        verifiyIfCategoryExists(categoryId);
        categoryRepository.deleteById(categoryId);
    }
    
    @Transactional
    public Category updateCategoryName(Category category) {
        verifiyIfCategoryExists(category.getId());
        categoryRepository.updateCategoryName(category.getId(), category.getName());
        return category;
    }
}