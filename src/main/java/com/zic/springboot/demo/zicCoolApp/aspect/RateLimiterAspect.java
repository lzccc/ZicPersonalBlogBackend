package com.zic.springboot.demo.zicCoolApp.aspect;

import com.zic.springboot.demo.zicCoolApp.exceptions.TooManyRequestsException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;


@Aspect
@Component
public class RateLimiterAspect {
    private static final Logger logger = LoggerFactory.getLogger(RateLimiterAspect.class);
    @Autowired
    HttpServletRequest httpServletRequest;
    private static final Map<String, Queue<Long>> requestHistory = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        logger.info("Starting backend rate limiter cleaner");
        startCleanupThreadV2();
    }

    private void startCleanupThreadV1() {
        //The TimerTask will be running in a seperate thread, this is a non-blocking operation
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                logger.debug("Remove all record in Ratelimiter hashMap to avoid OOM");
                requestHistory.clear();
            }
        }, 0, 100000);
    }

    //After Java 5, it is recommended to use ScheduledThreadPool for scheduled tasks.
    private void startCleanupThreadV2() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(() -> {
            logger.debug("Remove all record in Ratelimiter hashMap to avoid OOM");
            requestHistory.clear();
        }, 0, 100, TimeUnit.SECONDS);
    }

    @Around("@annotation(rateLimiter)")
    public Object applyRateLimit(ProceedingJoinPoint proceedingJoinPoint, RateLimiter rateLimiter) throws Throwable {
        String ipAddress = httpServletRequest.getHeader("X-Real-IP");
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        String recordKey = ipAddress + "--" + className + "." + methodName;
        Queue<Long> requests = requestHistory.computeIfAbsent(recordKey, k -> new ConcurrentLinkedQueue<>());

        long currentTime = System.currentTimeMillis();
        // Remove expired requests from the history
        while (!requests.isEmpty() && requests.peek() <= currentTime - rateLimiter.timeWindow()) {
            requests.poll();
        }
        if (requests.size() >= rateLimiter.value()) {
            logger.warn("Too many request for key: " + recordKey);
            throw new TooManyRequestsException("Too many requests. Please try again later.");
        }
        requests.offer(currentTime);
        return proceedingJoinPoint.proceed();
    }

}
