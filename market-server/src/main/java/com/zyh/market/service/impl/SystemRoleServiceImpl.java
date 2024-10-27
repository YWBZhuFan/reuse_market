package com.zyh.market.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.market.dto.SystemRolePageDto;
import com.zyh.market.entity.SystemRole;
import com.zyh.market.mapper.SystemRoleMapper;
import com.zyh.market.service.SystemRoleService;
import org.springframework.stereotype.Service;

/**
 * @author zhangyihua
 * @version 1.0
 * @description TODO
 * @date 2024/2/19 20:22
 */
@Service
public class SystemRoleServiceImpl extends ServiceImpl<SystemRoleMapper, SystemRole> implements SystemRoleService {
  @Override
  public Page getRolePageList(SystemRolePageDto rolePageDto) {
    Page<SystemRole> page = lambdaQuery()
      .like(StrUtil.isNotEmpty(rolePageDto.getKey()),SystemRole::getRoleCode, rolePageDto.getKey()).or()
      .like(StrUtil.isNotEmpty(rolePageDto.getKey()),SystemRole::getRoleName, rolePageDto.getKey())
      .orderByDesc(SystemRole::getCreateTime)
      .page(new Page<>(rolePageDto.getPageNumber(), rolePageDto.getPageSize()));
    return page;
  }
}
