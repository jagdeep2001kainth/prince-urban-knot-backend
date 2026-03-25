package com.jagdeep.princeurbanknot.repository;

import com.jagdeep.princeurbanknot.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}