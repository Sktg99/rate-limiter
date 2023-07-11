package com.shelfassessment.ratelimiter.interceptor;

import com.shelfassessment.ratelimiter.constant.Constant;
import com.shelfassessment.ratelimiter.exception.RateLimiterException;
import com.shelfassessment.ratelimiter.exception.UserServiceException;
import com.shelfassessment.ratelimiter.model.BucketType;
import com.shelfassessment.ratelimiter.service.RateLimiter;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    @Autowired
    RateLimiter rateLimiter;
    private static final Logger log = LoggerFactory.getLogger(RateLimitInterceptor.class);
    private static final String userIdQueryParam = "userId";
    private static final String baseURL = "/v1";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug(String.format("Request received: %s", request));
        if (request.getRequestURI().startsWith(baseURL)) {
            String userId = request.getParameter(userIdQueryParam);
            if (StringUtils.isNotBlank(userId)) {
                // Global rate limiter
                String globalKey = generateBucketKey(baseURL, userId);
                consumeToken(userId, globalKey, BucketType.USER_GLOBAL,
                        Constant.apiTokenUsageMapping.get(BucketType.USER_GLOBAL));
                // API level rate limiter
                String apiKey = generateBucketKey(request.getRequestURI(), userId);
                consumeToken(userId, apiKey, BucketType.USER_API,
                        Constant.apiTokenUsageMapping.get(BucketType.USER_API));
            } else {
                String errorMsg = "Query param userId should not be blank";
                log.error(errorMsg);
                throw new UserServiceException(errorMsg);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        // failure case to restore tokens
        if(response.getStatus() == 500) {
            String userId = request.getParameter(userIdQueryParam);
            String globalKey = generateBucketKey(baseURL, userId);
            Bucket bucket = rateLimiter.resolveBucket(globalKey, BucketType.USER_GLOBAL);
            bucket.addTokens(Constant.apiTokenUsageMapping.get(BucketType.USER_GLOBAL));

            String apiKey = generateBucketKey(request.getRequestURI(), userId);
            bucket = rateLimiter.resolveBucket(apiKey, BucketType.USER_API);
            bucket.addTokens(Constant.apiTokenUsageMapping.get(BucketType.USER_API));
        }
    }

    private void consumeToken(String userId, String key, BucketType bucketType, int permits) {
        Bucket bucket = rateLimiter.resolveBucket(key, bucketType);
        if (bucket.tryConsume(permits)) {
            log.info(String.format("Bucket token of type %s is consumed for the user %s", bucketType, userId));
        } else {
            String errorMsg = String.format("Received too many requests of type %s for the user %s", bucketType, userId);
            log.error(errorMsg);
            // adding back consumed global token in case of API level throttling
            if(BucketType.USER_API.equals(bucketType)){
                restoreGlobalBucket(userId);
            }
            throw new RateLimiterException(errorMsg);
        }
    }

    private void restoreGlobalBucket(String userId) {
        String key = generateBucketKey(baseURL, userId);
        Bucket globalBucket = rateLimiter.resolveBucket(key, BucketType.USER_GLOBAL);
        globalBucket.addTokens(Constant.apiTokenUsageMapping.get(BucketType.USER_GLOBAL));
        log.info(String.format("Added %d tokens to bucket of type %s for the user %s",
                Constant.apiTokenUsageMapping.get(BucketType.USER_GLOBAL), BucketType.USER_GLOBAL, userId));
    }

    private String generateBucketKey(String requestURI, String userId) {
        return requestURI.concat(userId);
    }
}
