package com.assignment_two_starter.controller;

import com.assignment_two_starter.config.JwtUtil;
import com.assignment_two_starter.model.entities.Address;
import com.assignment_two_starter.model.entities.Customer;
import com.assignment_two_starter.model.services.AddressService;
import com.assignment_two_starter.model.services.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    private final CustomerService customerService;

    private final JwtUtil jwtUtil;

    public AddressController(AddressService addressService, CustomerService customerService, JwtUtil jwtUtil) {
        this.addressService = addressService;
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAddress(@RequestBody Address address, HttpServletRequest request) {
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

        address.setUserId(customer);

        Address newAddress = addressService.saveAddress(address);

        return ResponseEntity.ok(newAddress);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateAddress(@RequestBody Address address, HttpServletRequest request) {
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

        address.setUserId(customer);

        Address updatedAddress = addressService.updateAddress(address);

        return ResponseEntity.ok(updatedAddress);
    }

    @GetMapping("/")
    public ResponseEntity<?> getCustomerAddresses(HttpServletRequest request) {
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

        return ResponseEntity.ok(addressService.getAddressesByUserId(customer.getUserId()));
    }

    @PostMapping("/delete/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable Integer addressId, HttpServletRequest request) {
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

        Address address = addressService.getAddressById(addressId);

        if(address == null) {
            return ResponseEntity.badRequest().body("Address not found");
        }

        if (!address.getUserId().equals(customer)) {
            return ResponseEntity.badRequest().body("Address not found");
        }

        addressService.deleteAddress(addressId);

        return ResponseEntity.ok("Address deleted successfully");
    }
}
