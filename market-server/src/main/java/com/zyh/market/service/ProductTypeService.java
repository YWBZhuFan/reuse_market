package com.zyh.market.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.market.dto.SystemProductTypePageDto;
import com.zyh.market.entity.ProductOrder;
import com.zyh.market.entity.ProductType;


public interface ProductTypeService extends IService<ProductType> {
  Page getTypePageList(SystemProductTypePageDto dto);
}
