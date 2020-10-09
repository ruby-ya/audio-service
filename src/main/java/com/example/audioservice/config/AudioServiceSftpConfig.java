package com.example.audioservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ruby
 * @create 2020-10-02 10:15:27
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "audio.service.sftp")
public class AudioServiceSftpConfig {
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String path;
}
