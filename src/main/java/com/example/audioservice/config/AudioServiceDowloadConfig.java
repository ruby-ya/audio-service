package com.example.audioservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ruby
 * @create 2020-10-02 10:21:10
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "audio.service.download")
public class AudioServiceDowloadConfig {
    private String path;
}
