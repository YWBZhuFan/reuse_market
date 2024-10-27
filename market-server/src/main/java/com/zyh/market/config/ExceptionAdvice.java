package com.zyh.market.config;

import cn.dev33.satoken.exception.NotLoginException;

import cn.dev33.satoken.exception.NotRoleException;
import com.zyh.market.R;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.constants.AuthCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zhangyihua
 * @version 1.0
 * @description TODO
 * @date 2023/7/7 11:54
 */

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public R<String> handleException(Exception e) {
        log.error("系统异常", e);
        return R.fail(ResultCode.SystemError);
    }
    @ExceptionHandler(ServiceException.class)
    public R<String> handleBusinessException(ServiceException e) {
        log.error("业务异常", e);
        return R.fail(e.getResultCode());
    }
    @ExceptionHandler(NotLoginException.class)
    public R<String> handleNotLogin(NotLoginException e){
        log.error("未登录", e);
        return R.fail(AuthCode.USER_PERMISSION_UNAUTHENTICATED);
    }
    
    @ExceptionHandler(NotRoleException.class)
    public R<String> handleNotRole(NotRoleException e) {
        log.error("角色异常", e);
        return R.fail(AuthCode.USER_PERMISSION_UNAUTHORIZED);
    }
}
