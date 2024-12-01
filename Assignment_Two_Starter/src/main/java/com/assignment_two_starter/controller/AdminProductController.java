package com.assignment_two_starter.controller;

import com.assignment_two_starter.model.dto.ProductDTO;
import com.assignment_two_starter.model.entities.Product;
import com.assignment_two_starter.model.services.EmailService;
import com.assignment_two_starter.model.services.ProductService;
import com.assignment_two_starter.model.services.ShoppingCartService;
import jakarta.validation.Valid;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final EmailService emailService;
    private final ProductService productService;
    private final ShoppingCartService shoppingCartService;

    public AdminProductController(EmailService emailService, ProductService productService, ShoppingCartService shoppingCartService) {
        this.emailService = emailService;
        this.productService = productService;
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping("/update/{productId}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Integer productId,
            @Valid @RequestBody ProductDTO productDTO) {
        Product product = productService.updateProduct(productId, productDTO);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/images/{productId}")
    public ResponseEntity<String> uploadProductImages(
            @PathVariable Integer productId,
            @RequestParam(value = "existingImages", required = false) List<String> existingImages,
            @RequestParam(value = "newImages", required = false) List<MultipartFile> newImages) {
        try {
            String relativePath = "static/assets/images/large/" + productId;
            File directory = new File(new ClassPathResource(relativePath).getFile().getAbsolutePath());

            // Ensure the directory exists
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Handle existing images: Remove only images not in the `existingImages` list
            if (existingImages != null) {
                File[] currentFiles = directory.listFiles();
                if (currentFiles != null) {
                    for (File file : currentFiles) {
                        String fileName = file.getName();
                        if (!existingImages.contains(fileName)) {
                            // Delete the file if it is not in the `existingImages` list
                            file.delete();
                            System.out.println("Deleted image: " + fileName);
                        }
                    }
                }
            }

            // Save new images
            if (newImages != null && !newImages.isEmpty()) {
                for (int i = 0; i < newImages.size(); i++) {
                    MultipartFile image = newImages.get(i);
                    File destination = new File(directory, productId + "_" + (i + 1) + ".jpg");
                    image.transferTo(destination); // Save file
                    System.out.println("Saved to destination: " + destination);
                }
            }

            return ResponseEntity.ok("Images updated successfully");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading images: " + e.getMessage());
        }
    }



    @PostMapping("/archive/{productId}")
    public ResponseEntity<?> archiveProduct(@PathVariable Integer productId) {
        Product product = productService.getProductById(productId);

        List<String> affectedUsers = shoppingCartService.RemoveProductsFromCart(product);

        affectedUsers.forEach(userEmail -> {
            emailService.sendEmail(
                    userEmail,
                    "Product Archived: " + product.getName(),
                    "<h1>Product Archived!</h1>"
                            + "<p>Dear Customer,</p>"
                            + "<p>We regret to inform you that the product <strong>"
                            + product.getName() + "</strong> has been removed from our store and as such, been removed from your shopping cart.</p>"
                            + "<p>Thank you for your understanding.</p>"
                            + "<p>Best regards,</p>"
                            + "<p><strong>The BabyNest Team</strong></p>",
                    true
            );
        });

        productService.archiveProduct(productId);

        return ResponseEntity.ok("Product archived");
    }

    @PostMapping("/unarchive/{productId}")
    public ResponseEntity<?> unarchiveProduct(@PathVariable Integer productId) {
        productService.unarchiveProduct(productId);
        return ResponseEntity.ok("Product unarchived");
    }

    @GetMapping("/archived")
    public ResponseEntity<List<Product>> getArchivedProducts() {
        List<Product> archivedProducts = productService.getArchivedProducts();
        if (archivedProducts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(archivedProducts);
    }
}
