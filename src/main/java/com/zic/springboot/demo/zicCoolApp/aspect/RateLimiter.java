package com.zic.springboot.demo.zicCoolApp.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimiter {
    int value(); // Number of requests allowed within the time window
    long timeWindow(); // Time window in milliseconds
}
