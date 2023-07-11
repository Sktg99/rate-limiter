package com.shelfassessment.ratelimiter.service;

import com.shelfassessment.ratelimiter.factory.BucketConfigurationFactory;
import com.shelfassessment.ratelimiter.model.BucketType;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Supplier;

@Service
public class RateLimiter {
    @Autowired
    ProxyManager<String> proxyManager;

    public Bucket resolveBucket(String key, BucketType bucketType) {
        Supplier<BucketConfiguration> configSupplier = BucketConfigurationFactory.getBucketConfiguration(bucketType);
        return proxyManager.builder().build(key, configSupplier);
    }

}
