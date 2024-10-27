package com.zyh.market.exception;


import com.zyh.market.constants.ResultCode;
import lombok.Data;

/**
 * @author zhangyihua
 * @version 1.0
 * @description TODO
 * @date 2023/7/8 19:38
 */
@Data
public class ServiceException extends RuntimeException {
    ResultCode resultCode;

    public ServiceException(ResultCode resultCode) {
        super(resultCode.getName());
        this.resultCode = resultCode;
    }

    public ServiceException(ResultCode resultCode, String msg) {
        super(msg);
        resultCode.setName(msg);
        this.resultCode = resultCode;

    }
}
