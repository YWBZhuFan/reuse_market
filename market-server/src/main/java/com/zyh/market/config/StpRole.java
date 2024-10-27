package com.zyh.market.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.zyh.market.entity.SystemRole;
import com.zyh.market.entity.SystemUser;
import com.zyh.market.service.SystemRoleService;
import com.zyh.market.service.SystemUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyihua
 * @version 1.0
 * @description TODO
 * @date 2024/2/19 20:44
 */
@Component
public class StpRole implements StpInterface {

  @Autowired SystemUserService systemUserService;
  @Autowired SystemRoleService systemRoleService;
  
  @Override
  public List<String> getRoleList(Object loginId, String loginType) {
    String userId = StpUtil.getLoginIdAsString();
    SystemUser systemUser = systemUserService.getById(userId);
    
    List<SystemRole> systemRoleList = systemRoleService.lambdaQuery().eq(SystemRole::getId,systemUser.getRoleId()).list();
    List<String> roleList = new ArrayList<String>();
    for (SystemRole systemRole : systemRoleList) {
      roleList.add(systemRole.getRoleCode());
    }
    return roleList;
  }
  
  @Override
  public List<String> getPermissionList(Object loginId, String loginType) {
    return null;
  }
}
