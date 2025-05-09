package com.qualitas.portal.accountfraudesapi.configuracion.ratelimit;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RateLimiter {
    private static final Logger logger = LoggerFactory.getLogger(RateLimiter.class);
    private final ConcurrentHashMap<String, List<Long>> requestTimestamps = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS = 120; // 2 requests per minute
    private static final long WINDOW_SIZE_MS = 60_000; // 1 minute in milliseconds

    public synchronized boolean tryConsume(String key) {
        long now = Instant.now().toEpochMilli();
        List<Long> timestamps = requestTimestamps.computeIfAbsent(key, k -> new ArrayList<>());
        
        // Limpiar timestamps antiguos
        timestamps.removeIf(timestamp -> now - timestamp >= WINDOW_SIZE_MS);
        
        logger.info("IP: {}, Current requests in window: {}, Window size: {} ms", 
                   key, timestamps.size(), WINDOW_SIZE_MS);

        if (timestamps.size() >= MAX_REQUESTS) {
            long oldestRequest = timestamps.get(0);
            long timeToWait = WINDOW_SIZE_MS - (now - oldestRequest);
            logger.warn("Rate limit exceeded for IP: {}. Need to wait {} ms", 
                       key, Math.max(0, timeToWait));
            return false;
        }

        timestamps.add(now);
        logger.info("Request allowed for IP: {}. Total requests in current window: {}", 
                   key, timestamps.size());
        return true;
    }

    // Método para limpiar entradas antiguas
    public void cleanup() {
        long now = Instant.now().toEpochMilli();
        requestTimestamps.entrySet().removeIf(entry -> {
            entry.getValue().removeIf(timestamp -> now - timestamp >= WINDOW_SIZE_MS);
            return entry.getValue().isEmpty();
        });
    }
} 