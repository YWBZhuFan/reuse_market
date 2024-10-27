package com.zyh.market.controller.admin;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.market.R;
import com.zyh.market.dto.SystemProductInfoPageDto;
import com.zyh.market.service.ProductInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/product/info")
@SaCheckLogin
@SaCheckRole(value = {"admin"})
public class ProductInfoAdminController {

  @Autowired ProductInfoService productInfoService;
  
  @GetMapping("/page")
  public R<Page> getProductInfoList(SystemProductInfoPageDto dto) {
    Page page = productInfoService.getProductInfoList(dto);
    return R.ok(page);
  }
  @GetMapping("/detail/{id}")
  public R<Map> getDetail(@PathVariable("id")String id){
    Map map = productInfoService.getDetail(id);
    return R.ok(map);
  }
  @PutMapping("/pass/{id}")
  public R passProduct(@PathVariable("id") String id){
    productInfoService.passProduct(id);
    return R.ok();
  }
  
  @PutMapping("/fail/{id}")
  public R failProduct(@PathVariable("id") String id) {
    productInfoService.failProduct(id);
    return R.ok();
  }
  
  @PutMapping("/down/{id}")
  public R downProduct(@PathVariable("id") String id) {
    productInfoService.downProduct(id);
    return R.ok();
  }
}
