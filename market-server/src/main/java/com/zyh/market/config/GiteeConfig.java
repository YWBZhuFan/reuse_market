package com.zyh.market.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "gitee")
public class GiteeConfig {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
