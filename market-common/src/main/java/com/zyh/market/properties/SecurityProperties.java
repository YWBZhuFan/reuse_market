package com.zyh.market.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    private Boolean enabled = true;
}
