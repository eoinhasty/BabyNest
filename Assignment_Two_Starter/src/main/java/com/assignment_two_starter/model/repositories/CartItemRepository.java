package com.assignment_two_starter.model.repositories;

import com.assignment_two_starter.model.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
}
