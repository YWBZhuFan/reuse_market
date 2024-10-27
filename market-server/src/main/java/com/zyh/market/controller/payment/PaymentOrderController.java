package com.zyh.market.controller.payment;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.bean.BeanUtil;
import com.zyh.market.R;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.dto.PaymentOrderDto;
import com.zyh.market.entity.PaymentOrder;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.service.PaymentOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment/order")
@SaCheckLogin
public class PaymentOrderController {
  @Autowired private PaymentOrderService paymentOrderService;
  @PostMapping
  public R<String> createPaymentOrder(@RequestBody PaymentOrderDto dto){
    String id = paymentOrderService.createPaymentOrder(dto.getOrderId());
    return R.ok(id);
  }
  @GetMapping
  public R<PaymentOrder> getPaymentOrder(String paymentOrderId){
    PaymentOrder paymentOrder = paymentOrderService.getById(paymentOrderId);
    if(BeanUtil.isEmpty(paymentOrder)) throw new ServiceException(ResultCode.NotFindError);
    return R.ok(paymentOrder);
  }
}
