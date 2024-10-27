package com.zyh.market.mqlistener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.zyh.market.entity.*;
import com.zyh.market.mapper.PointsDetailMapper;
import com.zyh.market.service.*;
import com.zyh.market.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

import static com.zyh.market.constants.MySqlConstants.*;
import static com.zyh.market.constants.MySqlConstants.POST_SELF_CODE;
import static com.zyh.market.constants.RedisConstants.USER_POINTS_PREFIX;

@Slf4j
@Component
public class PayListener {

    @Autowired private ProductOrderService productOrderService;
    @Autowired private UserServiceImpl userService;
    @Autowired private StringRedisTemplate redisTemplate;
    @Autowired private PointsDetailMapper pointsDetailMapper;
    @Autowired private PaymentPayService paymentPayService;
    @Autowired private PaymentOrderService paymentOrderService;
    @Autowired private ProductInfoService productInfoService;

    @Transactional
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "pay.success.order.queue", durable = "true"),
            exchange = @Exchange(name = "pay.exchange"),
            key = "pay.success.order"))
    public void updateProductStatus(String orderNumber){
        log.info("支付成功，更新订单状态");
        ProductOrder productOrder = productOrderService.query().eq(ORDER_NUMBER, orderNumber).one();
        if (BeanUtil.isEmpty(productOrder) || productOrder.getPayStatus() == 9){
            return;
        }
        productOrderService.update().eq(ORDER_NUMBER,orderNumber)
                .set(PAY_STATUS, 9)
                .set(DEAL_STATUS, 3)
                .set(POST_SELF_CODE, RandomUtil.randomNumbers(4)).update();
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "pay.success.points.queue", durable = "true"),
            exchange = @Exchange(name = "pay.exchange"),
            key = "pay.success.points"
    ))
    public void updatePoints(Map<String, Object> map){
        log.info("支付成功，更新用户积分");
        double totalAmount = (Double) map.get("totalAmount");
        String userId = (String) map.get("userId");
        String productTitle = (String) map.get("productTitle");
        int range = determineRange(totalAmount);
        PointsDetail pointsDetail = new PointsDetail();
        switch (range) {
            case 1: pointsDetail.setPointsChange(10);break; // 小于50
            case 2: pointsDetail.setPointsChange(20);break; // 大于等于50且小于100
            case 3: pointsDetail.setPointsChange(55);break; // 大于等于100且小于300
            case 4: pointsDetail.setPointsChange(85);break; // 大于等于300且小于500
            case 5: pointsDetail.setPointsChange(100);break; // 大于等于300且小于500
        }
        pointsDetail.setUserId(userId);
        pointsDetail.setType(2);
        User user = userService.query().eq(ID, userId).one();
        user.setPoints(user.getPoints()+pointsDetail.getPointsChange());
        pointsDetail.setPointsTotal(user.getPoints());
        pointsDetail.setProductTitle(productTitle);
        pointsDetail.setCreateTime(LocalDateTime.now());
        userService.update().eq(ID, user.getId()).set("points", user.getPoints()).update();
        redisTemplate.delete(USER_POINTS_PREFIX + user.getId());
        pointsDetailMapper.insert(pointsDetail);
        if (!StrUtil.isEmpty(user.getInviterId())){
            PointsDetail pointsDetail2 = new PointsDetail();
            int range2 = determineRange(totalAmount);
            switch (range2) {
                case 1: pointsDetail2.setPointsChange(5);break; // 小于50
                case 2: pointsDetail2.setPointsChange(10);break; // 大于等于50且小于100
                case 3: pointsDetail2.setPointsChange(25);break; // 大于等于100且小于300
                case 4: pointsDetail2.setPointsChange(40);break; // 大于等于300且小于500
                case 5: pointsDetail2.setPointsChange(55);break; // 大于等于300且小于500
            }
            pointsDetail2.setUserId(user.getInviterId());
            pointsDetail2.setType(3);
            pointsDetail.setCreateTime(LocalDateTime.now());
            User user2 = userService.query().eq(ID, user.getInviterId()).one();
            if (BeanUtil.isEmpty(user2)){throw new RuntimeException("邀请人信息错误");}
            user2.setPoints(user2.getPoints()+pointsDetail2.getPointsChange());
            pointsDetail2.setPointsTotal(user2.getPoints());
            userService.update().update(user2);
            redisTemplate.delete(USER_POINTS_PREFIX + user2.getId());
            pointsDetailMapper.insert(pointsDetail2);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "pay.timeout.queue", durable = "true"),
            exchange = @Exchange(name = "delay.direct", delayed = "true"),
            key = "pay.timeout"
    ))
    public void OrderTimeOut(String orderId){
        ProductOrder productOrder = productOrderService.getById(orderId);
        if (BeanUtil.isEmpty(productOrder)){
            return;
        }
        String payOrderId = productOrder.getPayOrderId();
        PaymentOrder paymentOrder = paymentOrderService.getById(payOrderId);
        PaymentPay paymentPay = paymentPayService.query().eq(ORDER_ID, paymentOrder.getId()).one();
        if (paymentPay.getPaymentStatus() == 9 && paymentOrder.getPaymentStatus() == 9){
            if (productOrder.getPostSelfCode().isEmpty()){
                productOrderService.update().eq(ID,orderId).set(POST_SELF_CODE, RandomUtil.randomNumbers(4)).update();
            }
            return;
        }
        productOrderService.update().eq(ID, orderId).set(PAY_STATUS, 2).set(DEAL_STATUS, 1).set(UPDATE_TIME, LocalDateTime.now()).update();
        productInfoService.update().eq(ID, productOrder.getProductId()).set(STATUS, 9).update();
    }


    private static int determineRange(double totalAmount) {
        if (totalAmount >= 500.00) {
            return 5;
        } else if (totalAmount >= 300.00) {
            return 4;
        } else if (totalAmount >= 100.00) {
            return 3;
        } else if (totalAmount >= 50.00) {
            return 2;
        } else {
            return 1;
        }
    }
}
