package com.assignment_two_starter.controller;

import com.assignment_two_starter.model.entities.Review;
import com.assignment_two_starter.model.services.EmailService;
import com.assignment_two_starter.model.services.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/admin/reviews")
public class AdminReviewController {
    private final ReviewService reviewService;

    private final EmailService emailService;

    public AdminReviewController(ReviewService reviewService, EmailService emailService) {
        this.reviewService = reviewService;
        this.emailService = emailService;
    }

    @GetMapping
    public List<Review> getAllPendingReviews() {
        return reviewService.getPendingReviews();
    }

    @PostMapping("/{reviewId}/approve")
    public ResponseEntity<String> approveReview(@PathVariable Integer reviewId) {
        Review review = reviewService.getReviewById(reviewId);

        if (review == null) {
            return ResponseEntity.notFound().build();
        }

        if(reviewService.approveReview(reviewId) == null) {
            return ResponseEntity.badRequest().build();
        }

        String emailContent = "<h1>Review Approved!</h1>"
                + "<p>Dear " + review.getCustomer().getFirstName() + ",</p>"
                + "<p>We are pleased to inform you that your review for the product <strong>"
                + review.getProduct().getName() + "</strong> has been approved.</p>"
                + "<p>Here is your review:</p>"
                + "<blockquote style='font-style: italic; color: #555;'>"
                + review.getReviewText()
                + "</blockquote>"
                + "<p>Rating: " + "★".repeat(review.getRating()) + " (" + review.getRating() + "/5)</p>"
                + "<p>Thank you for sharing your thoughts!</p>"
                + "<p>Best regards,</p>"
                + "<p><strong>The BabyNest Team</strong></p>";

        emailService.sendEmail(
                review.getCustomer().getEmail(),
                "Review Approved: " + review.getProduct().getName(),
                emailContent,
                true
        );
        return ResponseEntity.ok("Review approved");
    }
}