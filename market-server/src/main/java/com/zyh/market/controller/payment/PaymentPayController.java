package com.zyh.market.controller.payment;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.zyh.market.R;
import com.zyh.market.dto.PaymentPayDto;
import com.zyh.market.service.PaymentPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment/pay")
@SaCheckLogin
public class PaymentPayController {

  @Autowired private PaymentPayService paymentPayService;
  @PostMapping
  public R createPaymentPay(@RequestBody PaymentPayDto dto){
    String id = paymentPayService.createPaymentPay(dto.getPaymentOrderId());
    return R.ok(id);
  }
  @PutMapping("/finish/{paymentPayId}")
  public R finishPay(@PathVariable("paymentPayId") String paymentPayId){
    paymentPayService.finishPay(paymentPayId);
    return R.ok();
  }

  @GetMapping("/check/{paymentPayId}")
  public R checkPay(@PathVariable("paymentPayId") String paymentPayId){
    return paymentPayService.checkPayStatus(paymentPayId);
  }
}
