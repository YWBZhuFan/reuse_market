package com.zyh.market.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.market.R;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.entity.PaymentOrder;
import com.zyh.market.entity.PaymentPay;
import com.zyh.market.entity.PaymentType;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.mapper.PaymentPayMapper;
import com.zyh.market.service.PaymentOrderService;
import com.zyh.market.service.PaymentPayService;
import com.zyh.market.service.PaymentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.zyh.market.constants.MySqlConstants.ORDER_ID;
import static com.zyh.market.constants.MySqlConstants.PAYMENT_STATUS;

@Service
public class PaymentPayServiceImpl extends ServiceImpl<PaymentPayMapper, PaymentPay> implements PaymentPayService {
  @Autowired private PaymentOrderService paymentOrderService;
  @Autowired private PaymentTypeService paymentTypeService;
  @Override
  @Transactional(rollbackFor = Exception.class)
  public String createPaymentPay(String paymentOrderId) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    PaymentOrder paymentOrder= getPayOrderInfo(paymentOrderId);
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.MINUTE, -5);
    Date failTime = cal.getTime();
    if (paymentOrder.getTimeCreate().getTime() < failTime.getTime()) throw new ServiceException(ResultCode.Fail,"支付超时");
    if (paymentOrder.getPayPrice() < 0) throw new ServiceException(ResultCode.Fail);
    PaymentType paymentType = paymentTypeService.getById(paymentOrder.getPayTypeId());
    if (BeanUtil.isEmpty(paymentType)) throw new ServiceException(ResultCode.BusinessError);
    PaymentPay pay = new PaymentPay();
    pay.setPaymentType("支付宝支付");
    pay.setPaymentPrice(paymentOrder.getPayPrice());
    pay.setUserId(paymentOrder.getUserId());
    pay.setOrderId(paymentOrder.getId());
    pay.setPaymentTimeStart(formatter.format(new Date()));
    Calendar cal1 = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.MINUTE, 5);
    Date expireTime = cal1.getTime();
 
    String format = formatter.format(expireTime);
    pay.setPaymentTimeExpire(format);
    pay.setTimeCreate(new DateTime());
    pay.setTimeUpdate(new DateTime());
    save(pay);
    paymentOrder.setPaymentPayId(pay.getId());
    paymentOrder.setPaymentStatus(1);
    paymentOrderService.updateById(paymentOrder);
    return pay.getId();
  }
  
  @Override
  public void finishPay(String paymentPayId) {
    PaymentPay paymentPay = getById(paymentPayId);
    if(BeanUtil.isEmpty(paymentPay)) throw new ServiceException(ResultCode.ValidateError);
    PaymentOrder paymentOrder = paymentOrderService.getById(paymentPay.getOrderId());
    if (BeanUtil.isEmpty(paymentOrder)) throw new ServiceException(ResultCode.ValidateError);
    paymentPay.setPaymentResultData("支付成功");
    if (paymentPay.getPaymentStatus() == 9 || paymentPay.getPaymentStatus() == 8) throw new ServiceException(ResultCode.Fail, "订单错误");
    paymentPay.setPaymentStatus(9);
    paymentPay.setProcessStatus(1);
    paymentPay.setTimeUpdate(new DateTime());
    
    boolean update = updateById(paymentPay);
  }

  @Override
  public R checkPayStatus(String paymentPayId) {
    boolean isSuccess = query().eq(ORDER_ID, paymentPayId).eq(PAYMENT_STATUS, 9).exists();
    if (!isSuccess){
      return R.fail(ResultCode.NotFindError);
    }
    return R.ok("支付成功");
  }

  public PaymentOrder getPayOrderInfo(String paymentOrderId) {
    PaymentOrder paymentOrder = paymentOrderService.getById(paymentOrderId);
    if (BeanUtil.isEmpty(paymentOrder)) throw new ServiceException(ResultCode.Fail, "订单不存在");
    if (paymentOrder.getOrderStatus() > 0) throw new ServiceException(ResultCode.Fail, "订单已锁定");
    if (!StpUtil.getLoginIdAsString().equals(paymentOrder.getUserId())) throw new ServiceException(ResultCode.Fail, "订单不存在");
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.MINUTE, -5);
    Date time = cal.getTime();
    if (paymentOrder.getTimeCreate().getTime() < time.getTime()) throw new ServiceException(ResultCode.Fail, "支付超时");
    return paymentOrder;
  }
}
