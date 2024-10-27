package com.zyh.market.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.zyh.market.R;
import com.zyh.market.service.VoucherOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/voucher/order")
@SaCheckLogin
public class VoucherOrderController {
  @Autowired private VoucherOrderService voucherOrderService;
  @PostMapping("/seckill/{id}")
  public R seckill(@PathVariable("id") String voucherId) {
    voucherOrderService.seckill(voucherId);
    return R.ok();
  }
}
