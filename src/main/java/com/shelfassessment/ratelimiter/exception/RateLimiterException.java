package com.shelfassessment.ratelimiter.exception;

import java.io.Serial;

public class RateLimiterException extends RuntimeException {
    public RateLimiterException(String string) {
        super(string);
    }
}
