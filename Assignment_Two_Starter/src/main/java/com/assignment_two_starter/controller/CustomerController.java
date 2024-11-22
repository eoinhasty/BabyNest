package com.assignment_two_starter.controller;

import com.assignment_two_starter.model.entities.Customer;
import com.assignment_two_starter.model.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

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
}
