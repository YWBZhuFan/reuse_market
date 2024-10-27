package com.zyh.market.controller.admin;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.market.R;
import com.zyh.market.dto.SystemProductOrderPageDto;
import com.zyh.market.service.ProductOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin/product/order")
@SaCheckLogin
@SaCheckRole(value = {"admin"})
public class ProductOrderAdminController {

  @Autowired private ProductOrderService productOrderService;
  
  @GetMapping("/page")
  public R<Page> getProductOrderList(SystemProductOrderPageDto dto) {
    Page page = productOrderService.getProductOrderList(dto);
    return R.ok(page);
  }
  
  @GetMapping("/detail/{id}")
  public R<Map> getDetail(@PathVariable("id") String id) {
    Map map = productOrderService.getOrderDetail(id);
    return R.ok(map);
  }
  
  @GetMapping("/page/evaluate")
  public R<Page> getProductOrderEvaluateList(SystemProductOrderPageDto dto) {
    Page page = productOrderService.getProductOrderEvaluateList(dto);
    return R.ok(page);
  }
}
