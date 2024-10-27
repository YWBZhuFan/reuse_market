package com.zyh.market.controller.admin;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.zyh.market.R;
import com.zyh.market.dto.SystemPaymentOrderPageDto;
import com.zyh.market.service.PaymentOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin/payment/order")
@SaCheckLogin
@SaCheckRole(value = {"admin"})
public class PaymentOrderAdminController {
  @Autowired PaymentOrderService paymentOrderService;
  
  @GetMapping("/page")
  public R<Page> getPaymentOrderList(SystemPaymentOrderPageDto dto) {
    Page page = paymentOrderService.getPaymentOrderList(dto);
    return R.ok(page);
  }
  
  @GetMapping("/detail/{id}")
  public R<Map> getDetail(@PathVariable("id") String id) {
    Map map = paymentOrderService.getOrderDetail(id);
    return R.ok(map);
  }
}
