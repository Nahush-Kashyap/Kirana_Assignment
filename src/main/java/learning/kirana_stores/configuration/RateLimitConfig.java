package learning.kirana_stores.configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.function.Supplier;

@Configuration
public class RateLimitConfig {
    // Autowiring dependencies
    @Autowired
    public ProxyManager buckets;

    /**
     * Resolves and returns a Bucket based on the provided key.
     * @param key The key used to identify the Bucket.
     * @return A Bucket instance associated with the provided key.
     */
    public Bucket resolveBucket(String key) {
        Supplier<BucketConfiguration> configSupplier = getConfigSupplierForUser(key);

        // Does not always create a new bucket, but instead returns the existing one if it exists.
        return buckets.builder().build(key, configSupplier);
    }

    /**
     * Generates a Supplier for BucketConfiguration based on the provided key.
     * @param key The key used to identify the user.
     * @return A Supplier of BucketConfiguration.
     */
    private Supplier<BucketConfiguration> getConfigSupplierForUser(String key) {
        // Define the refill strategy with 10 tokens every 10 minutes
        Refill refill = Refill.intervally(10, Duration.ofMinutes(10));

        // Define the overall bandwidth limit using classic bandwidth model
        Bandwidth limit = Bandwidth.classic(10, refill);

        // Return a Supplier of BucketConfiguration
        return () -> (BucketConfiguration.builder()
                .addLimit(limit)
                .build());
    }
}
