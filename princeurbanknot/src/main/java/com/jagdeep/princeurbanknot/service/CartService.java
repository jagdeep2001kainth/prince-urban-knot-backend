package com.jagdeep.princeurbanknot.service;

import com.jagdeep.princeurbanknot.model.*;
import com.jagdeep.princeurbanknot.repository.*;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       UserRepository userRepository,
                       ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // Get or create cart for user
    public Cart getCartForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(Cart.builder().user(user).build()));
    }

    // Add item to cart
    public Cart addToCart(String email, Long productId, Integer quantity) {
        Cart cart = getCartForUser(email);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if product already in cart
        cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + quantity),
                        () -> cart.getItems().add(
                                CartItem.builder()
                                        .cart(cart)
                                        .product(product)
                                        .quantity(quantity)
                                        .build()
                        )
                );

        return cartRepository.save(cart);
    }

    // Remove item from cart
    public Cart removeFromCart(String email, Long cartItemId) {
        Cart cart = getCartForUser(email);
        cart.getItems().removeIf(item -> item.getId().equals(cartItemId));
        return cartRepository.save(cart);
    }

    // Clear entire cart
    public void clearCart(String email) {
        Cart cart = getCartForUser(email);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}