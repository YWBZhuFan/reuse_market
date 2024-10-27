package com.zyh.market.service.impl;

import com.zyh.market.service.SocialLoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SocialLoginContext {

    private Map<String, SocialLoginStrategy> strategies = new HashMap<>();

    @Autowired
    public SocialLoginContext(List<SocialLoginStrategy> socialLoginStrategies) {
        for (SocialLoginStrategy strategy : socialLoginStrategies) {
            strategies.put(strategy.getClass().getSimpleName(), strategy);
        }
    }

    public SocialLoginStrategy getStrategy(String provider) {
        return strategies.get(provider);
    }
}