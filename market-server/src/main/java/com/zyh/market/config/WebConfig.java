package com.zyh.market.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import com.zyh.market.Interceptors.ClickCountInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.zyh.market.constants.Constants.WebOriginsConstants.*;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
          .allowedOrigins(LOCALHOST8080, LOCALHOST9528, ONLINE, ONLINE8080, ONLINE9528, "http://47.238.210.25:9528") // 允许跨域的请求
          .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
          .maxAge(3600)
          .allowCredentials(true);
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**"); //Sa-Token拦截器
        registry.addInterceptor(new ClickCountInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/chat/list/noRead/total")
                .excludePathPatterns("/admin/**")
                .excludePathPatterns("/public/admin/clickCount")
                .excludePathPatterns("/chat/list/all"); //点击次数拦截器
    }
}
