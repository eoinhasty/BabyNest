package com.assignment_two_starter.model.repositories;

import com.assignment_two_starter.model.entities.CartItem;
import com.assignment_two_starter.model.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByProduct(Product product);
}
