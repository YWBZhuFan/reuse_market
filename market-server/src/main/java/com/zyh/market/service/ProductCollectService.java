package com.zyh.market.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.market.R;
import com.zyh.market.entity.ProductCollect;


public interface ProductCollectService extends IService<ProductCollect> {
  R delete(String id);
}
