package com.jagdeep.princeurbanknot.controller;

import com.jagdeep.princeurbanknot.model.Product;
import com.jagdeep.princeurbanknot.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductBulkV3Controller {

    private final ProductRepository productRepository;

    public ProductBulkV3Controller(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping("/bulk-v3")
    public ResponseEntity<?> bulkUploadV3(@RequestBody List<Product> products) {
        try {
            System.out.println("DEBUG bulk-v3 hit, received " + products.size() + " products");

            // Make sure imageUrl (legacy field) is populated from imageUrls
            // even if the frontend didn't set it explicitly.
            for (Product p : products) {
                if (p.getImageUrl() == null && p.getImageUrls() != null && !p.getImageUrls().isEmpty()) {
                    p.setImageUrl(p.getImageUrls().get(0));
                }
            }

            List<Product> saved = productRepository.saveAll(products);

            System.out.println("DEBUG bulk-v3 saved " + saved.size() + " products successfully");

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "count", saved.size()
            ));
        } catch (Exception e) {
            System.out.println("DEBUG bulk-v3 error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }
}