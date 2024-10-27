package com.zyh.market.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.market.R;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.dto.PublicPageDto;
import com.zyh.market.dto.UserExchangeAddressDto;
import com.zyh.market.entity.UserExchange;
import com.zyh.market.mapper.UserExchangeMapper;
import com.zyh.market.service.UserExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserExchangeServiceImpl extends ServiceImpl<UserExchangeMapper, UserExchange> implements UserExchangeService {

    @Autowired private UserExchangeMapper userExchangeMapper;

    /**
     * 上传用户兑换地址
     * @param userExchangeAddressDto
     * @return
     */
    @Override
    public R uploadExchangeAddress(UserExchangeAddressDto userExchangeAddressDto) {
        if (!StrUtil.isNotBlank(userExchangeAddressDto.getId())){
            return R.fail(ResultCode.Fail);
        }
        String userId = StpUtil.getLoginIdAsString();
        userExchangeAddressDto.setUserId(userId);
        userExchangeAddressDto.setUpdateTime(LocalDateTime.now());
        Boolean isSuccess = userExchangeMapper.uploadUserExchangeAddress(userExchangeAddressDto);
        if (!isSuccess){
            return R.fail(ResultCode.Fail, "上传失败，请稍后重试");
        }
        return R.ok("上传成功");
    }

    /**
     * 获取平台兑换列表
     * @param dto
     * @return
     */
    @Override
    public Page getExchangePage(PublicPageDto dto) {
        Page<UserExchange> page = lambdaQuery()
                .like(StrUtil.isNotEmpty(dto.getKey()), UserExchange::getMallProductTitle, dto.getKey()).or()
                .like(StrUtil.isNotEmpty(dto.getKey()), UserExchange::getNickName, dto.getKey())
                .orderByDesc(UserExchange::getUpdateTime)
                .page(new Page<>(dto.getPageNumber(), dto.getPageSize()));
        return page;
    }
}
