package com.performance.sut.service;

import com.performance.sut.entity.User;
import com.performance.sut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final SimulationService simulationService;

    @Transactional(readOnly = true)
    public User getUser(@NonNull Long id) {
        log.info("Fetching user with id: {}", id);
        // Simulate read latency
        simulationService.simulateLatency();
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
}
