package com.zyh.market.aop;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.domain.Person;
import com.zyh.market.R;
import com.zyh.market.annotations.ReSubmit;
import com.zyh.market.constants.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.zyh.market.constants.Constants.OtherConstants.RESUBMIT_KEY;

@Aspect
@Slf4j
@Component
public class ReSubmitAop {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Pointcut(value = "@annotation(com.zyh.market.annotations.ReSubmit)")
    public void pointCutReSubmit(){
    }

    @Around("execution(* com.zyh.market.controller..*(..)) && @annotation(com.zyh.market.annotations.ReSubmit))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ReSubmit reSubmit = method.getAnnotation(ReSubmit.class);
        String publishType = reSubmit.PublishType();
        String key = RESUBMIT_KEY + publishType + ":" + StpUtil.getLoginIdAsString();
        String s = redisTemplate.opsForValue().get(key);
        if (StrUtil.isEmpty(s)){
            redisTemplate.opsForValue().set(key, "1", 20, TimeUnit.SECONDS);
            return joinPoint.proceed();
        }
        return R.fail(ResultCode.ReSubmit);
    }
}
