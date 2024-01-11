package learning.kirana_stores.model.ratelimiter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenBucket {
    private int numberOfTokenAvailable;
    private int windowSizeForRateLimitInMilliSeconds;
    private int numberOfRequests;
    private long lastRefillTime;
    private long nextRefillTime;
    private int maxBucketSize;

    public TokenBucket (int maxBucketSize, int numberOfRequests, int windowSizeForRateLimitInMilliSeconds) {
    }
}
