package com.assignment_two_starter.controller;

import com.assignment_two_starter.config.JwtUtil;
import com.assignment_two_starter.model.entities.Customer;
import com.assignment_two_starter.model.entities.Orders;
import com.assignment_two_starter.model.services.CustomerService;
import com.assignment_two_starter.model.services.OrdersService;
import com.assignment_two_starter.model.services.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/orders")
public class OrdersController {

    OrdersService ordersService;

    CustomerService customerService;

    ShoppingCartService shoppingCartService;

    JwtUtil jwtUtil;

    public OrdersController(OrdersService ordersService, CustomerService customerService, ShoppingCartService shoppingCartService, JwtUtil jwtUtil) {
        this.ordersService = ordersService;
        this.customerService = customerService;
        this.shoppingCartService = shoppingCartService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Integer> requestBody, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }

        String token = authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(token);

        if (email == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        Customer customer = customerService.getCustomerByEmail(email);

        if (customer == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Integer cartId = shoppingCartService.getOrCreateCart(customer).getCartId();
        Integer addressId = requestBody.get("addressId");

        if(addressId == null) {
            return ResponseEntity.badRequest().body("Address not found");
        }

        try {
            Orders order = ordersService.createOrder(cartId, addressId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getOrders")
    public ResponseEntity<?> getOrders(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }

        String token = authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(token);

        if (email == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        Customer customer = customerService.getCustomerByEmail(email);

        if (customer == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        return ResponseEntity.ok(ordersService.getOrdersByCustomerId(customer.getUserId()));
    }

    @GetMapping("/getOrder/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Integer orderId, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }

        String token = authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(token);

        if (email == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        Customer customer = customerService.getCustomerByEmail(email);

        if (customer == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Orders order = ordersService.getOrderById(orderId);

        if (order == null) {
            return ResponseEntity.badRequest().body("Order not found");
        }

        if (!order.getCustomer().equals(customer)) {
            return ResponseEntity.badRequest().body("Order not found");
        }

        return ResponseEntity.ok(order);
    }
}
