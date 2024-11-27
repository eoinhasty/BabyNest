package com.assignment_two_starter.controller;

import com.assignment_two_starter.config.JwtUtil;
import com.assignment_two_starter.model.entities.Customer;
import com.assignment_two_starter.model.entities.ShoppingCart;
import com.assignment_two_starter.model.services.CustomerService;
import com.assignment_two_starter.model.services.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/")
    public ResponseEntity<?> getCart(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }

        String token = authorizationHeader.substring(7);

        String email = jwtUtil.extractUsername(token);

        if (email == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        Customer customer = customerService.getCustomerByEmail(email);

        if(customer == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        ShoppingCart cart = shoppingCartService.getOrCreateCart(customer);
        return ResponseEntity.ok(cart);
    }


    @PostMapping("/update/{cartItemId}")
    public ResponseEntity<?> updateCartItemQuantity(@PathVariable Integer cartItemId, @RequestBody Map<String, Integer> quantity, HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }

        String token = authorizationHeader.substring(7);

        String email = jwtUtil.extractUsername(token);

        if (email == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        Customer customer = customerService.getCustomerByEmail(email);

        if(customer == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        try{
            ShoppingCart cart = shoppingCartService.updateCartItemQuantity(cartItemId, quantity.get("quantity"));
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Integer> product, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }

        String token = authorizationHeader.substring(7);

        String email = jwtUtil.extractUsername(token);
        if(email == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        Customer customer = customerService.getCustomerByEmail(email);
        if(customer == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Integer productId = product.get("productId");
        Integer quantity = product.get("quantity");

        try {
            ShoppingCart cart = shoppingCartService.addProductToCart(customer, productId, quantity);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            if(e.getClass() == IllegalArgumentException.class) {
                return ResponseEntity.status(406).build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeFromCart(@RequestBody Map<String, Integer> cartItem, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }

        String token = authorizationHeader.substring(7);

        String email = jwtUtil.extractUsername(token);
        if(email == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        Customer customer = customerService.getCustomerByEmail(email);
        if(customer == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Integer cartItemId = cartItem.get("cartItemId");

        try {
            ShoppingCart cart = shoppingCartService.removeCartItem(cartItemId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/clear")
    public ResponseEntity<?> clearCart(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }

        String token = authorizationHeader.substring(7);

        String email = jwtUtil.extractUsername(token);
        if(email == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        Customer customer = customerService.getCustomerByEmail(email);
        if(customer == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        try {
            ShoppingCart cart = shoppingCartService.clearCart(customer);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
