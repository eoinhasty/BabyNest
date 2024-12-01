package com.assignment_two_starter.controller;

import com.assignment_two_starter.model.entities.Product;
import com.assignment_two_starter.model.entities.Review;
import com.assignment_two_starter.model.services.ProductService;
import com.assignment_two_starter.model.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.io.File;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getUnarchivedProducts();
        if (products == null || products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer productId) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @GetMapping("/{productId}/images")
    public ResponseEntity<List<String>> getProductImages(@PathVariable Integer productId) throws IOException {
        File directory = new ClassPathResource("static/assets/images/large/" + productId).getFile();
        FilenameFilter jpgFilter = (dir, name) -> name.toLowerCase().endsWith(".jpg");
        String[] imageFiles = directory.list(jpgFilter);

        List<String> images = Arrays.asList(imageFiles);

        return ResponseEntity.ok(images);
    }

    @GetMapping("/{productId}/reviews")
    public ResponseEntity<List<Review>> getProductReviews(@PathVariable Integer productId) {
        List<Review> reviews = reviewService.getReviewsByProductId(productId);
        if (reviews == null || reviews.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reviews);
    }
}
