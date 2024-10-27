package com.zyh.market.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserExchangeAddressDto {
    private String id;
    private String userId;
    private String name;
    private String phone;
    private String address;
    private LocalDateTime updateTime;
}
