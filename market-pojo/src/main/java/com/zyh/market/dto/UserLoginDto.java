package com.zyh.market.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangyihua
 * @version 1.0
 * @description TODO
 * @date 2023/7/7 9:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {
    private String phone;
    private String province;
    private String city;
    private String password;
    private String code;
}
