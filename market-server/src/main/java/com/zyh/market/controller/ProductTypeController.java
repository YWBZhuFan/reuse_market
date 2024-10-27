package com.zyh.market.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.zyh.market.R;
import com.zyh.market.entity.ProductType;
import com.zyh.market.service.ProductTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product/type")
@SaCheckLogin
public class ProductTypeController {
  @Autowired private ProductTypeService productTypeService;
  @GetMapping
  public R<List<ProductType>> getTypeList(){
    List<ProductType> list = productTypeService.list();
    return R.ok(list);
  }
  
}
