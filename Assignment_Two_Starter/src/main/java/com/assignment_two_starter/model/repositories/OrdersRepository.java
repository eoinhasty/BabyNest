package com.assignment_two_starter.model.repositories;

import com.assignment_two_starter.model.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    Optional<List<Orders>> findByCustomer_UserId (Integer userId);
}
