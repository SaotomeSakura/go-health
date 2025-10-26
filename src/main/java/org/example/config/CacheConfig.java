package org.example.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import org.example.entity.TicketEntity;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {

    private final CacheProperties cacheProperties;

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(cacheProperties.getExpireAfterWrite())
                .refreshAfterWrite(cacheProperties.getRefreshAfterWrite())
                .maximumSize(500);
    }

    @Bean
    public CacheManager defaultCacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.setCacheLoader(new CacheLoader<Object, Object>() {
            @Override
            public Object load(Object key) {
                return null;
            }
        });
        manager.setCaffeine(caffeine);
        return manager;
    }

}

