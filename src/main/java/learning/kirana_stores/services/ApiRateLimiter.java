package learning.kirana_stores.services;

import learning.kirana_stores.model.ratelimiter.TokenBucket;
import org.springframework.beans.factory.annotation.Autowired;

public class ApiRateLimiter {

    private final TokenBucket tokenBucket;

    /**
     * @param tokenBucket
     */
    @Autowired
    public ApiRateLimiter (TokenBucket tokenBucket) {
        this.tokenBucket = tokenBucket;
        this.refill ();
    }

    /**
     *
     */
    private void refill () {
        if (System.currentTimeMillis () < tokenBucket.getNextRefillTime ()) {
            return;
        }
        tokenBucket.setNumberOfTokenAvailable (Math.min (tokenBucket.getMaxBucketSize (), tokenBucket.getNumberOfTokenAvailable ()));
        tokenBucket.setLastRefillTime (System.currentTimeMillis ());
        tokenBucket.setNextRefillTime (tokenBucket.getLastRefillTime () + tokenBucket.getWindowSizeForRateLimitInMilliSeconds ());
    }

    /**
     * @return
     */
    public boolean tryConsume () {
        refill ();
        if (tokenBucket.getNumberOfTokenAvailable () > 0) {
            tokenBucket.setNextRefillTime (tokenBucket.getNumberOfTokenAvailable () - 1);
            return true;
        }
        return false;
    }
}
