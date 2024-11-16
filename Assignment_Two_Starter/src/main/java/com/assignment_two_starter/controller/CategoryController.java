package com.assignment_two_starter.controller;

import com.assignment_two_starter.model.entities.Category;
import com.assignment_two_starter.model.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        if (categories == null || categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categories);
    }
}
