package com.jagdeep.princeurbanknot.controller;

import com.jagdeep.princeurbanknot.model.Cart;
import com.jagdeep.princeurbanknot.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // GET cart
    @GetMapping
    public ResponseEntity<Cart> getCart(@AuthenticationPrincipal String email) {
        return ResponseEntity.ok(cartService.getCartForUser(email));
    }

    // POST add item
    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@AuthenticationPrincipal String email,
                                          @RequestBody Map<String, Object> request) {
        Long productId = Long.valueOf(request.get("productId").toString());
        Integer quantity = Integer.valueOf(request.get("quantity").toString());
        return ResponseEntity.ok(cartService.addToCart(email, productId, quantity));
    }

    // DELETE remove item
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<Cart> removeFromCart(@AuthenticationPrincipal String email,
                                               @PathVariable Long cartItemId) {
        return ResponseEntity.ok(cartService.removeFromCart(email, cartItemId));
    }

    // DELETE clear cart
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal String email) {
        cartService.clearCart(email);
        return ResponseEntity.noContent().build();
    }
}