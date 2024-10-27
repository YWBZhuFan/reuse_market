package com.zyh.market.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.market.dto.ProductVoucherCreateDto;
import com.zyh.market.entity.ProductType;
import com.zyh.market.entity.ProductVoucher;


public interface ProductVoucherService extends IService<ProductVoucher> {
  void create(ProductVoucherCreateDto dto);
  
}
