package com.zyh.market.task;

import com.xxl.job.core.handler.annotation.XxlJob;
import com.zyh.market.entity.ProductLike;
import com.zyh.market.service.ProductLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.zyh.market.constants.RedisConstants.LIKE_PRODUCT;

@Component
@EnableScheduling
@Slf4j
public class ProductLikeTask {

    @Autowired private ProductLikeService productLikeService;
    @Autowired private StringRedisTemplate redisTemplate;

    /**
     * 点赞数定时写入数据库任务
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    /*@XxlJob("taskProductLike")*/
    public void taskProductLike() {
        Set<String> keys = redisTemplate.keys(LIKE_PRODUCT.concat("*"));
        List<ProductLike> productLikeList = new ArrayList<>();
        if (keys == null || keys.isEmpty()) {
            log.info("没有需要写入的点赞数");
            return;
        }
        long num = 0;
        for (String key : keys) {
            String userId = key.split(":")[2];
            Set<String> valueSet = redisTemplate.opsForSet().members(key);
            for (String value : valueSet) {
                ProductLike productLike = new ProductLike().setProductId(value).setUserId(userId);
                productLikeList.add(productLike);
                num++;
            }
        }
        try {
            productLikeService.saveBatch(productLikeList);
        } catch (Exception e) {
            log.info("点赞数写入Redis失败");
        }finally {
            redisTemplate.delete(keys);
            log.info("{}个点赞数写入完毕",num);
        }
    }
}
