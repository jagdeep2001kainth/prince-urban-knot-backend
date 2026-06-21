package com.jagdeep.princeurbanknot.controller;

import com.jagdeep.princeurbanknot.model.Product;
import com.jagdeep.princeurbanknot.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductBulkV2Controller {

    private final ProductRepository productRepository;

    public ProductBulkV2Controller(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping("/bulk-v2")
    public ResponseEntity<?> bulkUploadV2(@RequestBody List<Product> products) {
        try {
            System.out.println("DEBUG bulk-v2 hit, received " + products.size() + " products");

            List<Product> saved = productRepository.saveAll(products);

            System.out.println("DEBUG bulk-v2 saved " + saved.size() + " products successfully");

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "count", saved.size()
            ));
        } catch (Exception e) {
            System.out.println("DEBUG bulk-v2 error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }
}