package com.jagdeep.princeurbanknot.repository;

import com.jagdeep.princeurbanknot.model.Order;
import com.jagdeep.princeurbanknot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}