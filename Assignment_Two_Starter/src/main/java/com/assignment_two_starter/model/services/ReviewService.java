package com.assignment_two_starter.model.services;

import com.assignment_two_starter.model.entities.Review;
import com.assignment_two_starter.model.repositories.ReviewRepository;

import java.util.List;
import java.util.Optional;

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
