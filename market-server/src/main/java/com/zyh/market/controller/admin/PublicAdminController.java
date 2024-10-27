package com.zyh.market.controller.admin;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.zyh.market.R;
import com.zyh.market.dto.SystemUserLoginDto;
import com.zyh.market.service.SystemUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhangyihua
 * @version 1.0
 * @description TODO
 * @date 2024/2/19 20:58
 */
@RestController
@RequestMapping("/public/admin")
public class PublicAdminController {
  @Autowired private SystemUserService systemUserService;
  
  @PostMapping("/user/login")
  public R<SaTokenInfo> login(@RequestBody SystemUserLoginDto request) {
    SaTokenInfo loginToken = systemUserService.login(request);
    return R.ok(loginToken);
  }


}
