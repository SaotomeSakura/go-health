package org.example.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cache.default")
public class CacheProperties {

    private Duration expireAfterWrite;
    private Duration refreshAfterWrite;

}
