package com.assignment_two_starter.controller;

import com.assignment_two_starter.model.services.CustomerService;
import com.assignment_two_starter.model.services.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/admin/users")
public class AdminUserController {
    private final CustomerService customerService;

    private final EmailService emailService;

    public AdminUserController(CustomerService customerService, EmailService emailService) {
        this.customerService = customerService;
        this.emailService = emailService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/suspended")
    public ResponseEntity<?> getSuspendedUsers() {
        return ResponseEntity.ok(customerService.getSuspendedCustomers());
    }

    @PostMapping("/suspend/{userId}")
    public ResponseEntity<?> suspendUser(@PathVariable Integer userId) {
        if(customerService.getCustomerById(userId) == null) {
            return ResponseEntity.notFound().build();
        }

        customerService.suspendAccount(userId);

        String emailContent = "<h1>Account Suspended</h1>"
                + "<p>Dear " + customerService.getCustomerById(userId).getFirstName() + ",</p>"
                + "<p>We regret to inform you that your account has been suspended.</p>"
                + "<p>If you believe this is an error, please contact us immediately.</p>"
                + "<p>Best regards,</p>"
                + "<p><strong>The BabyNest Team</strong></p>";


        emailService.sendEmail(
                customerService.getCustomerById(userId).getEmail(),
                "Account Suspended",
                emailContent,
                true
        );

        return ResponseEntity.ok("User suspended");
    }

    @PostMapping("/activate/{userId}")
    public ResponseEntity<?> activateUser(@PathVariable Integer userId) {
        if(customerService.getCustomerById(userId) == null) {
            return ResponseEntity.notFound().build();
        }

        customerService.activateAccount(userId);

        String emailContent = "<h1>Account Reactivated</h1>"
                + "<p>Dear " + customerService.getCustomerById(userId).getFirstName() + ",</p>"
                + "<p>We are pleased to inform you that your account has been reactivated.</p>"
                + "<p>Best regards,</p>"
                + "<p><strong>The BabyNest Team</strong></p>";

        emailService.sendEmail(
                customerService.getCustomerById(userId).getEmail(),
                "Account Reactivated",
                emailContent,
                true
        );

        return ResponseEntity.ok("User activated");
    }
}
