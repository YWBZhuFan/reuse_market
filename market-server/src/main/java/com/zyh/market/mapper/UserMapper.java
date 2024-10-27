package com.zyh.market.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.market.entity.User;
import org.apache.ibatis.annotations.Insert;

/**
* @author zhangyihua
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-01-28 20:50:46
* @Entity com.zyh.market.domain.User
*/

public interface UserMapper extends BaseMapper<User> {

}
