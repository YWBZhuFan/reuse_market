package com.zyh.market.task;

import com.zyh.market.constants.ClickCount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import static com.zyh.market.constants.RedisConstants.CLICK_COUNT;

@Component
@EnableScheduling
@Slf4j
public class ClickCountTask {

    @Autowired private StringRedisTemplate redisTemplate;

    /**
     * 点击量写入Redis定时任务
     */
    @Scheduled(cron = "0 */2 * * * *")
    public void taskClickCount() {
        redisTemplate.opsForValue().set(CLICK_COUNT,String.valueOf(ClickCount.getClickCount()));
    }
}
