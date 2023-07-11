package com.shelfassessment.ratelimiter.factory;

import com.shelfassessment.ratelimiter.constant.Constant;
import com.shelfassessment.ratelimiter.model.BucketType;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;

import java.util.function.Supplier;

public class BucketConfigurationFactory {
    public static Supplier<BucketConfiguration> getBucketConfiguration(BucketType bucketType) {
        Refill refill;
        Bandwidth limit;
        if (BucketType.USER_API.equals(bucketType)) {
            refill = Refill.intervally(Constant.apiRateLimitMapping.get(BucketType.USER_API),
                    Constant.apiRateLimitDurationMapping.get(BucketType.USER_API));
            limit = Bandwidth.classic(Constant.apiRateLimitMapping.get(BucketType.USER_API), refill);
            return () -> (BucketConfiguration.builder()
                    .addLimit(limit)
                    .build());
        }
        refill = Refill.intervally(Constant.apiRateLimitMapping.get(BucketType.USER_GLOBAL),
                Constant.apiRateLimitDurationMapping.get(BucketType.USER_GLOBAL));
        limit = Bandwidth.classic(Constant.apiRateLimitMapping.get(BucketType.USER_GLOBAL), refill);
        return () -> (BucketConfiguration.builder()
                .addLimit(limit)
                .build());
    }
}
