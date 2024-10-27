package com.zyh.market.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "alipay")
@Data
public class AlipayConfig {

    //支付宝AppId
    private String appId;
    //支付宝私钥
    private String appPrivateKey;
    //支付宝公钥
    private String alipayPublicKey;
    //异步回调地址
    private String notifyUrl;
    //支付宝回调前端路径
    private String returnUrl;
}