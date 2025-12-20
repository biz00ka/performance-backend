package com.performance.sut.controller;

import com.performance.sut.entity.Order;
import com.performance.sut.service.OrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getOrders(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(orderService.getOrders(userId, limit));
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request.getUserId(), request.getAmount()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Order> updateOrder(
            @PathVariable Long id,
            @RequestBody UpdateOrderRequest request) {
        return ResponseEntity.ok(orderService.updateOrder(id, request.getStatus()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @Data
    public static class CreateOrderRequest {
        private Long userId;
        private BigDecimal amount;
    }

    @Data
    public static class UpdateOrderRequest {
        private String status;
    }
}
