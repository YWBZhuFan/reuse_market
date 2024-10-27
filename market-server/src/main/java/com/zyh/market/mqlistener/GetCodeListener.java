package com.zyh.market.mqlistener;

import cn.hutool.core.util.RandomUtil;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.utils.AliGetCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.zyh.market.constants.RedisConstants.CHECK_CODE_PREFIX;

@Component
@Slf4j
public class GetCodeListener {

    @Autowired private StringRedisTemplate redisTemplate;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "code.queue", durable = "true"),
            exchange = @Exchange(name = "amq.direct"),
            key = "LoginCode.success"
    ))
    public void getCodeListener(String message){
        String[] split = message.split("-");
        String phone = split[1];
        //生成验证码
        String code = RandomUtil.randomNumbers(6);
        redisTemplate.opsForValue().set(CHECK_CODE_PREFIX + phone, code, 60, TimeUnit.SECONDS);
        log.info("验证码:{}", code);
        /*try {
          AliGetCodeUtil.SendCode(code,phone);
        } catch (Exception e) {
            log.error("验证码发送失败");
            throw new ServiceException(ResultCode.GetCodeError);
        }*/
    }
}
