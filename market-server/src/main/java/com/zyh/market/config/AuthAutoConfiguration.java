package com.zyh.market.config;

import com.zyh.market.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zhangyihua
 * @version 1.0
 * @description TODO
 * @date 2023/7/7 9:19
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
@ConditionalOnProperty(prefix = "security", name = "enabled", havingValue = "true")
public class AuthAutoConfiguration {
    @Configuration
    @Import(WebConfig.class)
    public static class SecurityConfig {
    }
}
