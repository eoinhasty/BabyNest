package com.assignment_two_starter.model.services;

import com.assignment_two_starter.model.dto.ProductDTO;
import com.assignment_two_starter.model.entities.Product;
import com.assignment_two_starter.model.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProductById(Integer id) {
        Optional<Product> c = productRepository.findById(id);
        if(c.isPresent())
            return c.get();
        else
            return null;
    }

    public List<Product> getUnarchivedProducts() {
        return productRepository.findByArchivedFalse();
    }

    public void createProduct(Product product) {
        productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Integer productId, ProductDTO productDTO) {
        Product product = getProductById(productId);
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setStockQuantity(productDTO.getStockQuantity());
        product.setDescription(productDTO.getDescription());
        product.setUpdatedAt(new Date());
        return productRepository.save(product);
    }

    public void clearExistingImages(Integer productId) throws IOException {
        File directory = new File("src/main/resources/static/assets/images/large/" + productId);
        if (directory.exists()) {
            for (File file : directory.listFiles()) {
                file.delete();
            }
        }
    }

    public void updateProductQuantity(Integer productId, int quantity) {
        Product product = getProductById(productId);
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);
    }

    public void archiveProduct(Integer productId) {
        Product product = getProductById(productId);
        product.setArchived(true);
        productRepository.save(product);
    }

    public void unarchiveProduct(Integer productId) {
        Product product = getProductById(productId);
        product.setArchived(false);
        productRepository.save(product);
    }

    public List<Product> getArchivedProducts() {
        return productRepository.findByArchivedTrue();
    }
}
