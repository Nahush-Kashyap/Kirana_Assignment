package learning.kirana_stores.configuration;

import com.giffing.bucket4j.spring.boot.starter.config.cache.SyncCacheResolver;
import com.giffing.bucket4j.spring.boot.starter.config.cache.jcache.JCacheCacheResolver;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.grid.jcache.JCacheProxyManager;
import org.redisson.config.Config;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.cache.CacheManager;
import javax.cache.Caching;

@Configuration
public class RedisConfig {
    /**
     * Bean to configure Redisson client.
     * @return Config instance for Redisson.
     */
    @Bean
    public Config config() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        return config;
    }

    /**
     * Bean to create and configure the CacheManager using Redisson.
     * @param config Redisson configuration.
     * @return CacheManager instance.
     */
    @Bean
    public CacheManager cacheManager1(Config config) {
        CacheManager manager = Caching.getCachingProvider().getCacheManager();
        manager.createCache("cache", RedissonConfiguration.fromConfig(config));
        return manager;
    }

    /**
     * Bean to create a ProxyManager using JCacheProxyManager.
     * @param cacheManager1 CacheManager instance.
     * @return ProxyManager instance.
     */
    @Bean
    ProxyManager<String> proxyManager(CacheManager cacheManager1) {
        return new JCacheProxyManager<>(cacheManager1.getCache("cache"));
    }

    /**
     * Primary bean for SyncCacheResolver using JCacheCacheResolver.
     * @param cacheManager1 CacheManager instance.
     * @return SyncCacheResolver instance.
     */
    @Bean
    @Primary
    public SyncCacheResolver bucket4jCacheResolver(CacheManager cacheManager1) {
        return new JCacheCacheResolver(cacheManager1);
    }
}
