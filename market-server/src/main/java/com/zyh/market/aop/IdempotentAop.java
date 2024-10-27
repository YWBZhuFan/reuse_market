package com.zyh.market.aop;

import cn.hutool.core.util.BooleanUtil;
import com.zyh.market.R;
import com.zyh.market.annotations.Idempotent;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.dto.ProductOrderDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class IdempotentAop {

    @Autowired private RedissonClient redissonClient;

    @Around("@annotation(idempotent)")
    public Object around(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
        Object[] args = joinPoint.getArgs();
        ProductOrderDto requestObject = (ProductOrderDto) args[0];
        String productId = requestObject.getProductId();
        String key = idempotent.keyPrefix() + ":" + productId;
        long expireTime = idempotent.expireTime();
        RLock lock = redissonClient.getLock(key);
        try {
            boolean isSuccess = lock.tryLock(0, expireTime, TimeUnit.SECONDS);
            if (BooleanUtil.isTrue(isSuccess)){
                return joinPoint.proceed();
            }
            return R.fail("已有买家下单");
        }catch (Exception e){
            return R.fail(ResultCode.SystemError);
        }
    }
}
