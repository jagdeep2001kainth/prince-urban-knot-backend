package com.jagdeep.princeurbanknot.repository;

import com.jagdeep.princeurbanknot.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}