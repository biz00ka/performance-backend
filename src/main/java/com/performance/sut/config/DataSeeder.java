package com.performance.sut.config;

import com.github.javafaker.Faker;
import com.performance.sut.entity.Order;
import com.performance.sut.entity.User;
import com.performance.sut.repository.OrderRepository;
import com.performance.sut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Value("${app.seeder.users:100}")
    private int userCount;

    @Value("${app.seeder.orders-per-user:5}")
    private int ordersPerUser;

    @Value("${app.seeder.enabled:true}")
    private boolean enabled;

    @Override
    public void run(String... args) throws Exception {
        if (!enabled || userRepository.count() > 0) {
            log.info("Data seeding skipped (disabled or data exists)");
            return;
        }

        log.info("Starting data seeding: {} users, {} orders/user", userCount, ordersPerUser);
        Faker faker = new Faker();
        List<User> users = new ArrayList<>();

        for (int i = 0; i < userCount; i++) {
            users.add(new User(faker.name().fullName(), faker.internet().emailAddress()));
        }
        users = userRepository.saveAll(users);
        log.info("Users seeded");

        List<Order> orders = new ArrayList<>();
        for (User user : users) {
            for (int j = 0; j < ordersPerUser; j++) {
                orders.add(new Order(
                        user,
                        BigDecimal.valueOf(faker.number().randomDouble(2, 10, 1000)),
                        Order.OrderStatus.values()[faker.random().nextInt(Order.OrderStatus.values().length)]));
            }
        }
        orderRepository.saveAll(orders);
        log.info("Orders seeded");
    }
}
