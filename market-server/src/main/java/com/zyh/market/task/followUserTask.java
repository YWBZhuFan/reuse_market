package com.zyh.market.task;

import com.zyh.market.R;
import com.zyh.market.entity.Follow;

import com.zyh.market.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.*;

import static com.zyh.market.constants.RedisConstants.FOLLOW_FOLLOW_USER;

@Component
@EnableScheduling
@Slf4j
public class followUserTask {

    @Autowired private StringRedisTemplate redisTemplate;
    @Autowired private FollowService followService;

    /**
     * 用户关注数写入数据库定时任务
     */
    @Scheduled(cron = "0 */2 * * * *")
    /*@XxlJob("taskFollowUser")*/
    public void taskFollowUser() {
        Set<String> keys = redisTemplate.keys(FOLLOW_FOLLOW_USER.concat("*"));
        if (keys == null || keys.isEmpty()){
            log.info("没有需要处理的关注数据");
            R.ok("没有需要处理的关注数据");
            return;
        }
        List<Follow> followList = new ArrayList<>();
        for (String key : keys) {
            String keyStr = key.split(":")[2];
            Set<String> valueSet = redisTemplate.opsForSet().members(key);
            if (valueSet != null) {
                valueSet.forEach(value -> {
                    Follow follow = new Follow();
                    follow.setUserId(keyStr);
                    follow.setFollowerId(value);
                    follow.setCreateTime(new Date().getTime());
                    followList.add(follow);
                });
            }
        }
        redisTemplate.delete(keys);
        followService.saveBatch(followList);
        log.info("关注数写入完毕");
        R.ok("关注数写入完毕");
    }
}
