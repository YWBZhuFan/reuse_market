package com.zyh.market.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "minio")
@Component
public class MinioProp {
  private String endpoint;
  private String accesskey;
  private String secretKey;
  private String bucket;
}
