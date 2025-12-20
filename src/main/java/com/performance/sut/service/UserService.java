package com.performance.sut.service;

import com.performance.sut.entity.User;
import com.performance.sut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final SimulationService simulationService;

    @Transactional(readOnly = true)
    @NonNull
    public User getUser(@NonNull Long id) {
        log.info("Fetching user with id: {}", id);
        // Simulate read latency
        simulationService.simulateLatency();
        return Objects.requireNonNull(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id)));
    }

    @Transactional
    @NonNull
    public User createUser(String name, String email) {
        log.info("Creating user with email: {}", email);
        simulationService.simulateLatency();
        User user = new User(name, email);
        return Objects.requireNonNull(userRepository.save(user));
    }

    @Transactional
    @NonNull
    public User updateUser(@NonNull Long id, String name, String email) {
        log.info("Updating user with id: {}", id);
        simulationService.simulateLatency();
        User user = getUser(id);
        if (name != null)
            user.setName(name);
        if (email != null)
            user.setEmail(email);
        return Objects.requireNonNull(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(@NonNull Long id) {
        log.info("Deleting user with id: {}", id);
        simulationService.simulateLatency();
        User user = getUser(id);
        userRepository.delete(user);
    }
}
