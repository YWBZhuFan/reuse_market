package com.zyh.market.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.market.dto.UserExchangeAddressDto;
import com.zyh.market.entity.UserExchange;
import org.apache.ibatis.annotations.Update;

public interface UserExchangeMapper extends BaseMapper<UserExchange> {
    @Update("update user_exchange set status = 2, post_address = #{address},post_name = #{name},post_phone = #{phone},update_time = #{updateTime} where id = #{id} and user_id = #{userId}")
    Boolean uploadUserExchangeAddress(UserExchangeAddressDto userExchangeAddressDto);
}
