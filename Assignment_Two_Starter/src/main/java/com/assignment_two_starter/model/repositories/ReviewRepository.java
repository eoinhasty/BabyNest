package com.assignment_two_starter.model.repositories;

import com.assignment_two_starter.model.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByApprovedFalseAndCustomer_LockedFalse();
    List<Review> findByProductProductIdAndApprovedTrue(Integer productId);
}
