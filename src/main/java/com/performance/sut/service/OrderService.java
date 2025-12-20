package com.performance.sut.service;

import com.performance.sut.entity.Order;
import com.performance.sut.entity.User;
import com.performance.sut.repository.OrderRepository;
import com.performance.sut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final SimulationService simulationService;

    @Transactional(readOnly = true)
    public List<Order> getOrders(Long userId, int limit) {
        log.info("Fetching orders for userId: {}, limit: {}", userId, limit);
        simulationService.simulateLatency();
        return orderRepository.findByUserIdAndNotDeleted(userId, PageRequest.of(0, limit)).getContent();
    }

    @Transactional
    public Order createOrder(Long userId, BigDecimal amount) {
        log.info("Creating order for userId: {}, amount: {}", userId, amount);
        simulationService.simulateLatency();

        User user = userRepository.findById(Objects.requireNonNull(userId))
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Order order = new Order(user, amount, Order.OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrder(Long orderId, String status) {
        log.info("Updating order: {}, status: {}", orderId, status);
        simulationService.simulateLatency();

        Order order = orderRepository.findById(Objects.requireNonNull(orderId))
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (order.isDeleted()) {
            throw new RuntimeException("Order is deleted: " + orderId);
        }

        order.setStatus(Order.OrderStatus.valueOf(status));
        return orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        log.info("Deleting order: {}", orderId);
        simulationService.simulateLatency();

        Order order = orderRepository.findById(Objects.requireNonNull(orderId))
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        order.setDeleted(true);
        order.setDeletedAt(LocalDateTime.now());
        orderRepository.save(order);
    }
}
