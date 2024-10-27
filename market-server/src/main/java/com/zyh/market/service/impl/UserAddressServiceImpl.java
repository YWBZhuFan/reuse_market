package com.zyh.market.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.market.entity.ProductOrder;
import com.zyh.market.entity.UserAddress;
import com.zyh.market.mapper.ProductOrderMapper;
import com.zyh.market.mapper.UserAddressMapper;
import com.zyh.market.service.ProductOrderService;
import com.zyh.market.service.UserAddressService;
import org.springframework.stereotype.Service;

@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {
}
