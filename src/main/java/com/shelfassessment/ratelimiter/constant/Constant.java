package com.shelfassessment.ratelimiter.constant;

import com.shelfassessment.ratelimiter.model.BucketType;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Constant {
    public static final int USER_GLOBAL_RATE_LIMIT = 2;
    public static final int USER_API_RATE_LIMIT = 1;
    public static Map<BucketType, Integer> apiRateLimitMapping;
    public static Map<BucketType, Duration> apiRateLimitDurationMapping;
    public static Map<BucketType, Integer> apiTokenUsageMapping;

    static {
        Map<BucketType, Integer> apiRateLimitMap = new HashMap<>();
        apiRateLimitMap.put(BucketType.USER_GLOBAL, USER_GLOBAL_RATE_LIMIT);
        apiRateLimitMap.put(BucketType.USER_API, USER_API_RATE_LIMIT);
        apiRateLimitMapping = apiRateLimitMap;

        Map<BucketType, Duration> apiRateLimitDurationMap = new HashMap<>();
        apiRateLimitDurationMap.put(BucketType.USER_GLOBAL, Duration.ofMinutes(2));
        apiRateLimitDurationMap.put(BucketType.USER_API, Duration.ofMinutes(2));
        apiRateLimitDurationMapping = apiRateLimitDurationMap;

        Map<BucketType, Integer> apiTokenUsageMap = new HashMap<>();
        apiTokenUsageMap.put(BucketType.USER_GLOBAL, 1);
        apiTokenUsageMap.put(BucketType.USER_API, 1);
        apiTokenUsageMapping = apiTokenUsageMap;
    }
}
