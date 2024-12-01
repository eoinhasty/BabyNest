package com.assignment_two_starter.model.services;

import com.assignment_two_starter.model.entities.Review;
import com.assignment_two_starter.model.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review getReviewById(Integer id) {
        Optional<Review> c = reviewRepository.findById(id);
        if(c.isPresent())
            return c.get();
        else
            return null;
    }

    public List<Review> getPendingReviews() {
        return reviewRepository.findByApprovedFalseAndCustomer_LockedFalse();
    }

    public List<Review> getReviewsByProductId(Integer productId) {
        return reviewRepository.findByProductProductIdAndApprovedTrue(productId);
    }

    public Review approveReview(Integer reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if(review.isPresent()) {
            Review r = review.get();
            r.setApproved(true);
            return reviewRepository.save(r);
        } else {
            return null;
        }
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public void createReview(Review review) {
        reviewRepository.save(review);
    }

    public void deleteReview(Review review) {
        reviewRepository.delete(review);
    }
}
