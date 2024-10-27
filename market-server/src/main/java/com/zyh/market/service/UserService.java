package com.zyh.market.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.market.R;
import com.zyh.market.dto.UpdateUserInfoDto;
import com.zyh.market.dto.UserAdminPageDto;
import com.zyh.market.dto.UserLoginDto;
import com.zyh.market.dto.UserRegisterDto;
import com.zyh.market.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
* @author zhangyihua
* @description 针对表【user】的数据库操作Service
* @createDate 2024-01-28 16:52:33
*/
public interface UserService extends IService<User> {
  SaTokenInfo login(UserLoginDto request);
  
  R<User> getUserInfo();

  R getLoginCode(String phone);
  
  R<User> getUserInfo(String userId);
  
  void updateUserInfoDetail(UpdateUserInfoDto dto);
  
  Page getUserList(UserAdminPageDto dto);
  
  SaTokenInfo loginPwd(UserLoginDto request);
  
  void updateUserInfo(UpdateUserInfoDto dto);
  
  Map getNum1UserStat();
  
  List<Map> getUserStat();

  R followUser(String id);

  R delFollowUser(String followUserId);

  R like(String productId);

  R unlike(String productId);

  R signIn();

  R getQRCodeShare();

  R isSignIn();

  R userRegister(UserRegisterDto userLoginDto);

  R getNewFollow();
}
