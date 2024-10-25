package com.assignment_two_starter.model.services;

import com.assignment_two_starter.model.entities.Category;
import com.assignment_two_starter.model.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category getCategoryById(Integer id) {
        Optional<Category> c = categoryRepository.findById(id);
        if(c.isPresent())
            return c.get();
        else
            return null;
    }

    public List<Category> getAllCategorys() {
        return categoryRepository.findAll();
    }

    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }
}
