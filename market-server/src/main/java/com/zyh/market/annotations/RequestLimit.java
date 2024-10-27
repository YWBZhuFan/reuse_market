package com.zyh.market.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestLimit {
    String keyPrefix() default "requestLimit";
    int permitsPerSecond();
    int maxBurstSeconds() default 1;
    int expireTimeInSeconds() default 60;
}