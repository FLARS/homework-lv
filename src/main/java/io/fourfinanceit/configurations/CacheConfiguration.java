package io.fourfinanceit.configurations;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.DefaultKeyGenerator;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    public CacheManager cacheManager() {
         return new ConcurrentMapCacheManager("ipAddressRequests") {
             @Override
             protected Cache createConcurrentMapCache(final String name) {
                 return new ConcurrentMapCache(
                         name,
                         CacheBuilder.newBuilder()
                                 .expireAfterWrite(24, TimeUnit.HOURS)
                                 .build()
                                 .asMap(),
                         false
                 );
             }
         };
    }
}
