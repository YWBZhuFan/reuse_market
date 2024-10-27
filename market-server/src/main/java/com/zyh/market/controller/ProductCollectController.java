package com.zyh.market.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.zyh.market.R;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.entity.ProductCollect;
import com.zyh.market.entity.UserAddress;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.service.ProductCollectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user/collect")
@SaCheckLogin
public class ProductCollectController {
  @Autowired private ProductCollectService productCollectService;
  
  @PostMapping
  public R create(@RequestBody ProductCollect collect) {
    ProductCollect one = productCollectService.lambdaQuery().eq(ProductCollect::getUserId, StpUtil.getLoginIdAsString())
      .eq(ProductCollect::getProductId, collect.getProductId()).one();
    if(!BeanUtil.isEmpty(one)) return R.ok();
    ProductCollect productCollect = new ProductCollect();
    productCollect.setUserId(StpUtil.getLoginIdAsString());
    productCollect.setProductId(collect.getProductId());
    productCollect.setCreateTime(new Date().getTime());
    productCollect.setUpdateTime(new Date().getTime());
    boolean save = productCollectService.save(productCollect);
    if (!save) throw new ServiceException(ResultCode.SaveError);
    return R.ok();
  }
  
  @DeleteMapping("/{id}")
  public R delete(@PathVariable String id) {
    return productCollectService.delete(id);
  }
  
  @GetMapping("/list")
  public R<List> list() {
    String userId = StpUtil.getLoginIdAsString();
    List<ProductCollect> list = productCollectService.lambdaQuery().eq(ProductCollect::getUserId, userId).list();
    ArrayList<Object> objects = new ArrayList<>();
    list.forEach(collect -> {
      objects.add(collect.getProductId());
    });
    return R.ok(objects);
  }
}
