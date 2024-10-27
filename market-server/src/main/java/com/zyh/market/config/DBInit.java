package com.zyh.market.config;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.zyh.market.entity.SystemRole;
import com.zyh.market.entity.SystemUser;
import com.zyh.market.service.SystemRoleService;
import com.zyh.market.service.SystemUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;

@Configuration
@Slf4j
public class DBInit {
  @Autowired private SystemRoleService systemRoleService;
  @Autowired private SystemUserService systemUserService;
  
  @PostConstruct
  @Transactional
  public void aaa() {
    SystemRole Role = systemRoleService.lambdaQuery().eq(SystemRole::getRoleCode, "system").one();
    if(null == Role){
      log.info("初始化管理员");
      SystemRole systemRole = new SystemRole();
      systemRole.setRoleCode("system");
      systemRole.setRoleName("系统管理员");
      systemRole.setCreateTime(new Date().getTime());
      systemRole.setUpdateTime(new Date().getTime());
      boolean save = systemRoleService.save(systemRole);
      SystemUser systemUser = new SystemUser();
      systemUser.setUsername("system");
      systemUser.setPassword(SaSecureUtil.md5("system"));
      systemUser.setRoleId(systemRole.getId());
      systemUser.setRoleCode("system");
      systemUser.setRoleName("系统管理员");
      systemUser.setName("系统管理员");
      systemUser.setCreateTime(new Date().getTime());
      systemUser.setUpdateTime(new Date().getTime());
      boolean save1 = systemUserService.save(systemUser);
    }
    SystemRole admin = systemRoleService.lambdaQuery().eq(SystemRole::getRoleCode, "admin").one();
    if (null == admin) {
      SystemRole systemRole1 = new SystemRole();
      systemRole1.setRoleCode("admin");
      systemRole1.setRoleName("超级管理员");
      systemRole1.setCreateTime(new Date().getTime());
      systemRole1.setUpdateTime(new Date().getTime());
      boolean save2 = systemRoleService.save(systemRole1);
      SystemUser systemUser = new SystemUser();
      systemUser.setUsername("admin");
      systemUser.setPassword(SaSecureUtil.md5("admin"));
      systemUser.setRoleId(systemRole1.getId());
      systemUser.setRoleCode("admin");
      systemUser.setRoleName("超级管理员");
      systemUser.setName("超级管理员");
      systemUser.setCreateTime(new Date().getTime());
      systemUser.setUpdateTime(new Date().getTime());
      boolean save3 = systemUserService.save(systemUser);
    }
  }
}
