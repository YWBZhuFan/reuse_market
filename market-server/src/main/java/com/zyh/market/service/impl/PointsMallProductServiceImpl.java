package com.zyh.market.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.market.R;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.dto.ProductInfoPageDto;
import com.zyh.market.entity.PointsDetail;
import com.zyh.market.entity.PointsMallProduct;
import com.zyh.market.entity.User;
import com.zyh.market.entity.UserExchange;
import com.zyh.market.mapper.PointsDetailMapper;
import com.zyh.market.mapper.PointsMallProductMapper;
import com.zyh.market.mapper.UserExchangeMapper;
import com.zyh.market.service.PointsMallProductService;
import com.zyh.market.service.UserExchangeService;
import com.zyh.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.zyh.market.constants.MySqlConstants.*;
import static com.zyh.market.constants.RedisConstants.*;

@Service
public class PointsMallProductServiceImpl extends ServiceImpl<PointsMallProductMapper, PointsMallProduct> implements PointsMallProductService {

    @Autowired private UserService userService;
    @Autowired private UserExchangeService userExchangeService;
    @Autowired private StringRedisTemplate redisTemplate;
    @Autowired private PointsDetailMapper pointsDetailMapper;
    @Autowired private UserExchangeMapper userExchangeMapper;
    @Autowired private PointsMallProductService pointsMallProductService;

    /**
     * 获取积分商品列表
     * @param pageDto
     * @return
     */
    @Override
    public List<PointsMallProduct> getPointsMallProductList(ProductInfoPageDto pageDto) {
        Page<PointsMallProduct> page = lambdaQuery()
                .eq(StrUtil.isNotEmpty(pageDto.getTypeCode()), PointsMallProduct::getTypeCode, pageDto.getTypeCode())
                .eq(PointsMallProduct::getStatus, 9)
                .and(StrUtil.isNotEmpty(pageDto.getKey()), wrapper -> {
                    wrapper.like(StrUtil.isNotEmpty(pageDto.getKey()), PointsMallProduct::getTitle, pageDto.getKey());
                })
                .orderByDesc(PointsMallProduct::getCreateTime)
                .page(new Page<>(pageDto.getPageNumber(), pageDto.getPageSize()));
        List<PointsMallProduct> records = page.getRecords();
        return records;
    }

    /**
     * 获取商品列表
     * @param pageDto
     * @return
     */
    @Override
    public R<List<PointsMallProduct>> getTypeList(ProductInfoPageDto pageDto) {
        String typeCode = pageDto.getTypeCode();
        String pointsMallProductKey = getPointsMallProductKey(typeCode);
        String productList = redisTemplate.opsForValue().get(pointsMallProductKey);
        if (!StrUtil.isEmpty(productList)){ return R.ok(JSONUtil.toList(productList, PointsMallProduct.class)); }
        List<PointsMallProduct> mallProduct;
        if (pointsMallProductKey.equals(PRODUCT_MALL_ALL)){
            mallProduct= pointsMallProductService.list();
        }else {
            mallProduct= pointsMallProductService.query().eq(TYPE_CODE, typeCode).list();
        }
        String jsonMallProduct = JSONUtil.toJsonStr(mallProduct);
        redisTemplate.opsForValue().set(pointsMallProductKey, jsonMallProduct);
        return R.ok(mallProduct);
    }

    public String getPointsMallProductKey(String typeCode){
        switch (typeCode){
            case "":
                return PRODUCT_MALL_ALL;
            case "1":
                return PRODUCT_MALL_MAKEUP;
            case "2":
                return PRODUCT_MALL_CARD;
            case "3":
                return PRODUCT_MALL_NUMERAL;
            case "4":
                return PRODUCT_MALL_FOOD;
        }
        return null;
    }

    /**
     * 兑换
     * @param productId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R exchange(Integer productId) {
        PointsMallProduct mallProduct = query().eq(ID, productId).one();
        Integer typeCode = mallProduct.getTypeCode();
        String image = mallProduct.getImage();
        String title = mallProduct.getTitle();
        Integer price = mallProduct.getPrice();
        String userId = StpUtil.getLoginIdAsString();
        User user = userService.query().eq(ID, userId).one();
        if (mallProduct.getStock() <= 0) {
            return R.fail(ResultCode.Fail, "库存不足");
        }
        if (user.getPoints() < mallProduct.getPrice()) {
            return R.fail(ResultCode.Fail, "您的积分不足");
        }
        try {
            userService.update().eq(ID, userId).set(POINTS, user.getPoints() - mallProduct.getPrice());
            update().set(EXCHANGE_NUMBER, mallProduct.getExchangeNumber() + 1)
                    .set(STOCK, mallProduct.getStock() - 1)
                    .eq(ID, productId).update();
            String exchangeProductId = getExchangeProductId();
            UserExchange userExchange = UserExchange.builder().id(exchangeProductId)
                    .nickName(user.getNickName())
                    .mallProductId(productId)
                    .mallProductPrice(price)
                    .mallProductType(typeCode)
                    .mallProductTitle(title)
                    .image(image)
                    .status(1)
                    .userId(userId)
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();
            userExchangeMapper.insert(userExchange);
            PointsDetail pointsDetail = new PointsDetail().setUserId(userId)
                    .setProductTitle(title)
                    .setPointsChange(-price)
                    .setType(4)
                    .setPointsTotal(user.getPoints() - mallProduct.getPrice())
                    .setCreateTime(LocalDateTime.now());
            pointsDetailMapper.insert(pointsDetail);
        }finally {
            redisTemplate.delete(USER_POINTS_PREFIX + userId);
        }
        return R.ok();
    }

    /**
     * 获取我的兑换列表
     * @return
     */
    @Override
    public R getMyExchangeList() {
        String s = redisTemplate.opsForValue().get(EXCHANGE_LIST + StpUtil.getLoginIdAsString());
        if (StrUtil.isNotBlank(s)) return R.ok(s);
        String userId = StpUtil.getLoginIdAsString();
        List<UserExchange> list = userExchangeService.lambdaQuery().eq(UserExchange::getUserId, userId).list();
        redisTemplate.opsForValue().set(EXCHANGE_LIST + StpUtil.getLoginIdAsString(), JSONUtil.toJsonStr(list));
        return R.ok(list);
    }

    /**
     * 查询用户积分
     * @return
     */
    @Override
    public Integer getUserPoints() {
        String userId = StpUtil.getLoginIdAsString();
        String pointsStr = redisTemplate.opsForValue().get(USER_POINTS_PREFIX + userId);
        if (!StrUtil.isEmpty(pointsStr)){
            return Integer.valueOf(pointsStr);
        }
        //根据userId查询用户积分
        User user = userService.query().eq(ID, userId).one();
        if (BeanUtil.isEmpty(user)){
            return null;
        }
        redisTemplate.opsForValue().set(USER_POINTS_PREFIX+userId, String.valueOf(user.getPoints()));
        return user.getPoints();
    }



    /**
     * 获取兑换商品id
     * @return
     */
    public String getExchangeProductId() {
        long currentTimeMillis = System.currentTimeMillis();
        return String.valueOf(currentTimeMillis);
    }
}
