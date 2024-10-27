package com.zyh.market.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.market.R;
import com.zyh.market.entity.PaymentPay;


public interface PaymentPayService extends IService<PaymentPay> {
  String createPaymentPay(String id);
  
  void finishPay(String paymentPayId);

  R checkPayStatus(String paymentPayId);
}
