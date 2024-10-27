package com.zyh.market.mapper;

import cn.hutool.core.date.DateTime;
import com.zyh.market.entity.ProductOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.market.vo.DigitalCountVo;

import java.util.Map;

public interface ProductOrderMapper extends BaseMapper<ProductOrder> {

    DigitalCountVo getDigitalCount();
}
