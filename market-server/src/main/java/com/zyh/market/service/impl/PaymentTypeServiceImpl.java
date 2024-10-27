package com.zyh.market.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.market.entity.PaymentPay;
import com.zyh.market.entity.PaymentType;
import com.zyh.market.mapper.PaymentPayMapper;
import com.zyh.market.mapper.PaymentTypeMapper;
import com.zyh.market.service.PaymentPayService;
import com.zyh.market.service.PaymentTypeService;
import org.springframework.stereotype.Service;

@Service
public class PaymentTypeServiceImpl extends ServiceImpl<PaymentTypeMapper, PaymentType> implements PaymentTypeService {
}
