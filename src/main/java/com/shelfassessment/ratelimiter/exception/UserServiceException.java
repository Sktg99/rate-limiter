package com.shelfassessment.ratelimiter.exception;

import java.io.Serial;

public class UserServiceException extends RuntimeException {
    public UserServiceException(String string) {
        super(string);
    }
}
