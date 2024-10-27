package com.zyh.market.controller.admin;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.market.R;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.dto.SystemUserCreateDto;
import com.zyh.market.dto.SystemUserPageDto;
import com.zyh.market.dto.SystemUserUpdateDto;
import com.zyh.market.entity.SystemUser;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.service.SystemUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhangyihua
 * @version 1.0
 * @description TODO
 * @date 2024/2/18 21:54
 */
@RestController
@RequestMapping("/admin/user")
@SaCheckLogin
@SaCheckRole(value = {"system","admin"},mode = SaMode.OR)
public class SystemUserAdminController {

  @Autowired SystemUserService systemUserService;
  @GetMapping("/getUserInfo")
  public R<SystemUser> getUserInfo() {
    return systemUserService.getUserInfo();
  }
  
  @GetMapping("/page")
  public R<Page> getUserPageList(SystemUserPageDto userPageDto) {
    Page page = systemUserService.getUserPageList(userPageDto);
    return R.ok(page);
  }
  
  @PostMapping
  public R create(@RequestBody SystemUserCreateDto dto) {
    systemUserService.create(dto);
    return R.ok();
  }
  
  @PutMapping
  public R edit(@RequestBody SystemUserUpdateDto dto){
   systemUserService.edit(dto);
    return R.ok();
  }
  @DeleteMapping
  public R delete(String id){
    SystemUser systemUser = systemUserService.getById(id);
    if(ObjectUtil.isEmpty(systemUser) )throw new ServiceException(ResultCode.Fail);
    boolean remove = systemUserService.removeById(id);
    if (!remove)
      throw new ServiceException(ResultCode.DeleteError);
    return R.ok();
  }
  
  @PostMapping("/logout")
  public R logout() {
    Object loginId = StpUtil.getLoginId();
    StpUtil.logout(loginId);
    return R.ok();
  }
}
