package learning.kirana_stores.services;

import learning.kirana_stores.model.ratelimiter.TokenBucket;
import org.springframework.beans.factory.annotation.Autowired;

public class ApiRateLimiter {

    private final TokenBucket tokenBucket;

    /**
     * Constructor for ApiRateLimiter.
     * @param tokenBucket The token bucket for rate limiting.
     */
    @Autowired
    public ApiRateLimiter(TokenBucket tokenBucket) {
        this.tokenBucket = tokenBucket;
        this.refill();
    }

    /**
     * Refills the token bucket if needed based on the next refill time.
     */
    private void refill() {
        if (System.currentTimeMillis() < tokenBucket.getNextRefillTime()) {
            return;
        }
        tokenBucket.setNumberOfTokenAvailable(Math.min(tokenBucket.getMaxBucketSize(), tokenBucket.getNumberOfTokenAvailable()));
        tokenBucket.setLastRefillTime(System.currentTimeMillis());
        tokenBucket.setNextRefillTime(tokenBucket.getLastRefillTime() + tokenBucket.getWindowSizeForRateLimitInMilliSeconds());
    }

    /**
     * Attempts to consume a token from the token bucket.
     * @return true if a token is successfully consumed, false otherwise.
     */
    public boolean tryConsume() {
        refill();
        if (tokenBucket.getNumberOfTokenAvailable() > 0) {
            tokenBucket.setNextRefillTime(tokenBucket.getNumberOfTokenAvailable() - 1);
            return true;
        }
        return false;
    }
}
