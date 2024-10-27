package com.zyh.market.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.zyh.market.config.AlipayConfig;
import com.zyh.market.entity.PaymentPay;
import com.zyh.market.entity.PointsDetail;
import com.zyh.market.entity.ProductOrder;
import com.zyh.market.entity.User;
import com.zyh.market.mapper.PointsDetailMapper;
import com.zyh.market.service.PaymentOrderService;
import com.zyh.market.service.PaymentPayService;
import com.zyh.market.service.ProductOrderService;
import com.zyh.market.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.zyh.market.constants.Constants.AliPayConstants.*;
import static com.zyh.market.constants.MySqlConstants.*;
import static com.zyh.market.constants.RedisConstants.USER_POINTS_PREFIX;

@Slf4j
@RestController
@RequestMapping("/alipay")
public class AliPayController {

    @Autowired private AlipayConfig alipayConfig;
    @Autowired private PaymentPayService paymentPayService;
    @Autowired private PaymentOrderService paymentOrderService;
    @Autowired private ProductOrderService productOrderService;
    @Autowired private RabbitTemplate rabbitTemplate;
    public static String NUMBER ="";
    @GetMapping("/pay")
    public void pay(String orderNumber, HttpServletResponse httpResponse) throws IOException {
        //查询订单信息
        PaymentPay paymentPay = paymentPayService.query().eq(ID, orderNumber).one();
        if (BeanUtil.isEmpty(paymentPay)) {
            log.info("订单不存在");
            return;
        }
        ProductOrder productOrder = productOrderService.query().eq(PAY_ORDER_ID, paymentPay.getOrderId()).one();
        NUMBER = productOrder.getPayOrderId();
        //创建Client，通用SDK提供的Client，负责调用支付宝的API
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, alipayConfig.getAppId(),
                alipayConfig.getAppPrivateKey(), FORMAT, CHARSET, alipayConfig.getAlipayPublicKey(), SIGN_TYPE);
        //创建Request并设置Request参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        //设置业务参数
        JSONObject bizContent = new JSONObject();
        bizContent.set(OUT_TRADE_NO, productOrder.getOrderNumber()); //闲宝生成的订单号
        bizContent.set(TIMEOUT_EXPRESS, "15m"); //设置支付超时时间
        Long buyMoney = productOrder.getBuyMoney();
        Double totalAmount = (double) buyMoney / 100;
        bizContent.set(TOTAL_AMOUNT, totalAmount); //订单实付金额
        bizContent.set(SUBJECT, productOrder.getProductTitle()); //支付商品标题
        bizContent.set(PRODUCT_CODE, "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());
        String returnUrl = alipayConfig.getReturnUrl();
        request.setReturnUrl(returnUrl+NUMBER); //支付完成后自动跳转本地页面的路径
        //执行请求，拿到响应的结果，返回给浏览器
        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody();//调用SDK生成表单
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form); //直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @PostMapping("/notify")
    @Transactional(rollbackFor = Exception.class)
    public void payNotify(HttpServletRequest request,HttpServletResponse response) throws AlipayApiException, ParseException {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")){
            HashMap<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }
            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, alipayConfig.getAlipayPublicKey(), "UTF-8");
            //支付宝验签
            if (checkSignature) {
                log.info("========支付宝异步回调信息========");
                log.info("交易名称："+ params.get("subject"));
                log.info("商品订单号：{}", params.get("out_trade_no"));
                log.info("支付宝流水号：{}", params.get("trade_no"));
                log.info("支付金额：{}", params.get("total_amount"));
                log.info("支付状态：{}", params.get("trade_status"));
                log.info("付款时间"+ params.get("gmt_payment"));
                String tradeNo = params.get("out_trade_no"); //订单编号
                String gmtPayment = params.get("gmt_payment"); //支付时间
                String payTitle = params.get("subject");
                double totalAmount = Double.parseDouble(params.get("total_amount"));
                /*String alipayTradeNo = params.get("trade_no"); //支付宝交易编号*/
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(gmtPayment);
                long timestamp = date.getTime();
                //更新订单状态为已支付，设置支付信息
                ProductOrder productOrder = productOrderService.query().eq(ORDER_NUMBER,tradeNo).one();
                if (BeanUtil.isEmpty(productOrder)){
                    return;
                }
                paymentOrderService.update().eq("id", productOrder.getPayOrderId())
                        .set(PAYMENT_STATUS, 9)
                        .set(ORDER_STATUS, 2)
                        .set(TIME_FINISH,date)
                        .set(PAY_TYPE_NAME, payTitle)
                        .set(PAYMENT_TYPE, "支付宝支付").update();
                paymentPayService.update().eq(ORDER_ID, productOrder.getPayOrderId())
                        .set(PAYMENT_STATUS, 9)
                        .set(PAYMENT_TIME_EXPIRE, timestamp)
                        .set(PAYMENT_TYPE, "alipay")
                        .set(TIME_FINISH, LocalDateTime.now())
                        .update();

                Map<String, Object> map = new HashMap<>();
                map.put("totalAmount", totalAmount);
                map.put("userId", productOrder.getUserId());
                map.put("productTitle", productOrder.getProductTitle());
                CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());
                cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
                    @Override
                    public void onFailure(Throwable ex) {
                        log.error("修改订单状态消息发送失败：{}", ex.getMessage());
                    }
                    @Override
                    public void onSuccess(CorrelationData.Confirm result) {
                        if (!result.isAck()){
                            log.error("收到ConfirmCallback消息修改订单状态发送失败：{}", result.getReason());
                        }
                        log.info("修改订单状态消息发送成功");
                    }
                });
                rabbitTemplate.convertAndSend("pay.exchange", "pay.success.order", tradeNo);
                rabbitTemplate.convertAndSend("pay.exchange", "pay.success.points", map);
            }
        }
    }
}
