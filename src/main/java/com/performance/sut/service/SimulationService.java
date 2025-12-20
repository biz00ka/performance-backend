package com.performance.sut.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class SimulationService {

    @Value("${app.simulation.latency-min-ms:20}")
    private int minLatencyMs;

    @Value("${app.simulation.latency-max-ms:300}")
    private int maxLatencyMs;

    @Value("${app.simulation.enabled:true}")
    private boolean simulationEnabled;

    /**
     * Injects artificial latency into the current thread.
     */
    public void simulateLatency() {
        if (!simulationEnabled) {
            return;
        }

        try {
            long latency = ThreadLocalRandom.current().nextLong(minLatencyMs, maxLatencyMs + 1);
            Thread.sleep(latency);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Simulation latency interrupted", e);
        }
    }
}
