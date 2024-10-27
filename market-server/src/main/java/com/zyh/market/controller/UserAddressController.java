package com.zyh.market.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.zyh.market.Att;
import com.zyh.market.R;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.entity.UserAddress;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user/address")
@SaCheckLogin
public class UserAddressController {
  @Autowired private UserAddressService userAddressService;
  
  @PostMapping
  public R create(@RequestBody UserAddress address){
    UserAddress userAddress = new UserAddress();
    userAddress.setUserId(StpUtil.getLoginIdAsString());
    userAddress.setAddress(address.getAddress());
    userAddress.setPhone(address.getPhone());
    userAddress.setName(address.getName());
    userAddress.setCreateTime(new Date().getTime());
    userAddress.setUpdateTime(new Date().getTime());
    boolean save = userAddressService.save(userAddress);
    if (!save) throw new ServiceException(ResultCode.SaveError);
    return R.ok();
  }
  
  @DeleteMapping("/{id}")
  public R delete(@PathVariable String id) {
    UserAddress userAddress = userAddressService.getById(id);
    if (null == userAddress) throw new ServiceException(ResultCode.NotFindError);
    if (!userAddress.getUserId().equals(StpUtil.getLoginIdAsString())) throw new ServiceException(ResultCode.NotFindError);
    boolean remove = userAddressService.removeById(id);
    if (!remove) throw new ServiceException(ResultCode.DeleteError);
    return R.ok();
  }
  
  @GetMapping("/list")
  public R<List> list() {
    String userId = StpUtil.getLoginIdAsString();
    List<UserAddress> list = userAddressService.lambdaQuery().eq(UserAddress::getUserId, userId).list();
    return R.ok(list);
  }
}
