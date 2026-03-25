package com.jagdeep.princeurbanknot.controller;

import com.jagdeep.princeurbanknot.model.Order;
import com.jagdeep.princeurbanknot.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // POST place order from cart
    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@AuthenticationPrincipal String email) {
        return ResponseEntity.ok(orderService.placeOrder(email));
    }

    // GET all orders for logged in user
    @GetMapping
    public ResponseEntity<List<Order>> getMyOrders(@AuthenticationPrincipal String email) {
        return ResponseEntity.ok(orderService.getOrdersForUser(email));
    }

    // GET single order
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // PUT update order status
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id,
                                              @RequestBody Map<String, String> request) {
        Order.OrderStatus status = Order.OrderStatus.valueOf(request.get("status"));
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }
}
