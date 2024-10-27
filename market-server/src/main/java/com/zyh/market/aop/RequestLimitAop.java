package com.zyh.market.aop;

import com.zyh.market.R;
import com.zyh.market.annotations.RequestLimit;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.utils.IpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Slf4j
@Component
public class RequestLimitAop {

    @Autowired
    private RedissonClient redissonClient;

    @Pointcut(value = "@annotation(com.zyh.market.annotations.RequestLimit)")
    public void pointCutRequestLimit(){
    }

    /**
     * 请求限流
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.zyh.market.controller..*(..)) && @annotation(com.zyh.market.annotations.RequestLimit)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("进入请求限流");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequestLimit requestLimit = method.getAnnotation(RequestLimit.class);
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String methodName = method.getName();
        String keyPrefix = requestLimit.keyPrefix();
        int maxBurstSeconds = requestLimit.maxBurstSeconds();
        int expireTime = requestLimit.expireTimeInSeconds();
        String clientIp = IpUtil.getClientIp(request);
        int permitsPerSecond = requestLimit.permitsPerSecond();
        String key = keyPrefix + ":" + methodName + ":" + clientIp;
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL, permitsPerSecond, maxBurstSeconds, RateIntervalUnit.SECONDS);
        boolean isSuccess = rateLimiter.tryAcquire(1, 1, TimeUnit.SECONDS);
        if (!isSuccess){
            return R.fail(ResultCode.FrequentRequests);
        }
        RMap<Object, Object> rMap = redissonClient.getMap(key);
        rMap.expire(expireTime, TimeUnit.SECONDS);
        return joinPoint.proceed();
    }
}
