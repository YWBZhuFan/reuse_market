package com.zyh.market.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.market.R;
import com.zyh.market.entity.PointsMallType;
import com.zyh.market.mapper.PointsMallTypeMapper;
import com.zyh.market.service.PointsMallTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.zyh.market.constants.RedisConstants.POINTS_MALL_TYPE;

@Service
public class PointsMallTypeServiceImpl extends ServiceImpl<PointsMallTypeMapper, PointsMallType> implements PointsMallTypeService {

    @Autowired private StringRedisTemplate redisTemplate;

    @Override
    public R<List<PointsMallType>> getTypeList() {
        String s = redisTemplate.opsForValue().get(POINTS_MALL_TYPE);
        if (!StrUtil.isEmpty(s)){
            List<PointsMallType> list = JSONUtil.toList(s, PointsMallType.class);
            return R.ok(list);
        }
        List<PointsMallType> list = list();
        redisTemplate.opsForValue().set(POINTS_MALL_TYPE,JSONUtil.toJsonStr(list));
        return R.ok(list);
    }
}
