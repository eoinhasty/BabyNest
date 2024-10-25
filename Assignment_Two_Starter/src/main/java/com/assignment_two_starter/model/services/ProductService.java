package com.assignment_two_starter.model.services;

import com.assignment_two_starter.model.entities.Product;
import com.assignment_two_starter.model.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getproductById(Integer id) {
        Optional<Product> c = productRepository.findById(id);
        if(c.isPresent())
            return c.get();
        else
            return null;
    }

    public List<Product> getAllproducts() {
        return productRepository.findAll();
    }

    public void createproduct(Product product) {
        productRepository.save(product);
    }

    public void deleteproduct(Product product) {
        productRepository.delete(product);
    }
}
