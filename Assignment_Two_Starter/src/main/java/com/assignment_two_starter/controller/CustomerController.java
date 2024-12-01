package com.assignment_two_starter.controller;

import com.assignment_two_starter.model.entities.Customer;
import com.assignment_two_starter.model.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        if (customers == null || customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(customers);
    }

    @PostMapping(value = "/registerCustomer", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Customer> registerCustomer(@RequestBody Customer customer) {
        System.out.println("Registering customer: " + customer);

        if(customerService.emailExists(customer.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        Customer newCustomer = customerService.registerCustomer(customer);
        if (newCustomer == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(newCustomer);
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");

        if (!newPassword.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            return ResponseEntity.badRequest().body("Password must contain at least 8 characters, including uppercase, lowercase, number, and special character.");
        }

        Customer customer = customerService.getCustomerByEmail(email);
        if (customer == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        customer.setPassword(passwordEncoder.encode(newPassword));
        customerService.save(customer);

        return ResponseEntity.ok("Password updated successfully");
    }

}
