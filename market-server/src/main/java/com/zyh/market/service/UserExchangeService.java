package com.zyh.market.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.market.R;
import com.zyh.market.dto.PublicPageDto;
import com.zyh.market.dto.UserExchangeAddressDto;
import com.zyh.market.entity.Follow;
import com.zyh.market.entity.UserExchange;


public interface UserExchangeService extends IService<UserExchange> {
    R uploadExchangeAddress(UserExchangeAddressDto userExchangeAddressDto);

    Page getExchangePage(PublicPageDto dto);
}
