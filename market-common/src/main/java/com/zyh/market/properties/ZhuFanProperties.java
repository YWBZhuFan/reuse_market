package com.zyh.market.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "zhufan.app")
public class ZhuFanProperties {
    private String logoPath;
    private String shareUrl;
}
