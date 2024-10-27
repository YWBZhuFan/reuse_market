package com.zyh.market.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.market.R;
import com.zyh.market.dto.ProductInfoPageDto;
import com.zyh.market.entity.PointsMallProduct;
import com.zyh.market.entity.UserExchange;


import java.util.List;

public interface PointsMallProductService extends IService<PointsMallProduct> {
    List<PointsMallProduct> getPointsMallProductList(ProductInfoPageDto pageDto);

    R exchange(Integer productId);

    R<List<UserExchange>> getMyExchangeList();

    Integer getUserPoints();

    R<List<PointsMallProduct>> getTypeList(ProductInfoPageDto pageDto);
}
