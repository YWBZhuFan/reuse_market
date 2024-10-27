package com.zyh.market.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.market.dto.SystemProductTypePageDto;
import com.zyh.market.entity.ProductOrder;
import com.zyh.market.entity.ProductType;
import com.zyh.market.entity.SystemRole;
import com.zyh.market.mapper.ProductOrderMapper;
import com.zyh.market.mapper.ProductTypeMapper;
import com.zyh.market.service.ProductTypeService;
import org.springframework.stereotype.Service;

@Service
public class ProductTypeServiceImpl extends ServiceImpl<ProductTypeMapper, ProductType> implements ProductTypeService {
  @Override
  public Page getTypePageList(SystemProductTypePageDto dto) {
    Page<ProductType> page = lambdaQuery()
      .like(StrUtil.isNotEmpty(dto.getKey()), ProductType::getTypeCode, dto.getKey()).or()
      .like(StrUtil.isNotEmpty(dto.getKey()), ProductType::getTypeName, dto.getKey())
      .orderByDesc(ProductType::getCreateTime)
      .page(new Page<>(dto.getPageNumber(), dto.getPageSize()));
    return page;
  }
}
