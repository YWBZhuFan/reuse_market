package com.zyh.market.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {
    private String phone;
    private String code;
    private String password;
    private String confirmPassword;
    private String province;
    private String city;
    private String shareCode;
}
