package com.zyh.market.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.market.Att;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.dto.SystemPaymentOrderPageDto;
import com.zyh.market.entity.*;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.mapper.PaymentOrderMapper;
import com.zyh.market.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;

import static com.zyh.market.constants.MySqlConstants.*;

@Service
@Slf4j
public class PaymentOrderServiceImpl extends ServiceImpl<PaymentOrderMapper, PaymentOrder> implements PaymentOrderService {
  @Autowired private UserService userService;
  @Autowired private ProductOrderService productOrderService;
  @Override
  @Transactional(rollbackFor = Exception.class)
  public String createPaymentOrder(String orderId) {
    User user = userService.getById(StpUtil.getLoginIdAsString());
    ProductOrder productOrder = productOrderService.getById(orderId);
    if (BeanUtil.isEmpty(productOrder)) throw new ServiceException(ResultCode.ValidateError);
    if (productOrder.getDealStatus() != 0) throw new ServiceException(ResultCode.ValidateError);
    PaymentOrder paymentOrder = new PaymentOrder();
    paymentOrder.setOrderNumber(RandomUtil.randomNumbers(12));
    paymentOrder.setUserId(user.getId());
    paymentOrder.setPayPrice(0L);
    paymentOrder.setPayTypeId(2L); //支付宝支付
    productOrder.setDealStatus(2);
    paymentOrder.setPayPrice(productOrder.getBuyMoney() + paymentOrder.getPayPrice());
    if (paymentOrder.getPayPrice() < 0) throw new ServiceException(ResultCode.Fail);
    paymentOrder.setPayTypeName(productOrder.getProductTitle());
    paymentOrder.setTimeCreate(new DateTime());
    paymentOrder.setTimeUpdate(new DateTime());
    paymentOrder.setProcessStatus(0);
    paymentOrder.setPaymentStatus(0);
    paymentOrder.setOrderStatus(0);
    save(paymentOrder);
    productOrder.setPayOrderId(paymentOrder.getId());
    productOrderService.updateById(productOrder);
    return paymentOrder.getId();
  }

  @Override
  public Page getPaymentOrderList(SystemPaymentOrderPageDto dto) {
    Page<PaymentOrder> page = lambdaQuery()
      .like(StrUtil.isNotEmpty(dto.getKey()), PaymentOrder::getOrderNumber, dto.getKey())
      .orderByDesc(PaymentOrder::getTimeCreate)
      .page(new Page<>(dto.getPageNumber(), dto.getPageSize()));
    return page;
  }
  
  @Override
  public Map getOrderDetail(String id) {
    HashMap<Object, Object> map = new HashMap<>();
    PaymentOrder paymentOrder = getById(id);
    if (BeanUtil.isEmpty(paymentOrder)) throw new ServiceException(ResultCode.NotFindError);
    User user = userService.getById(paymentOrder.getUserId());
    if (BeanUtil.isEmpty(user)) throw new ServiceException(ResultCode.NotFindError);
    map.put("paymentOrder",paymentOrder);
    map.put("user", user);
    return map;
  }
  
  private void update_mall(PaymentOrder paymentOrder) {
    paymentOrder.setProcessStatus(9);
    paymentOrder.setTimeFinish(new DateTime());
    /*paymentOrderService.updateById(paymentOrder);*/
    try {
      boolean isSuccess = update().eq(ID, paymentOrder.getId()).update(paymentOrder);
      if (!isSuccess) throw new ServiceException(ResultCode.UpdateError);
      productOrderService.update()
        .eq(PAY_ORDER_ID, paymentOrder.getId())
        .set(PAY_STATUS, 9)
        .set(DEAL_STATUS, 3)
        .update();
    } catch (Exception e) {
      log.error("支付信息更新失败!");
      throw new RuntimeException(e);
    }
  }
}
