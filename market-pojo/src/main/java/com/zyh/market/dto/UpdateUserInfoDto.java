package com.zyh.market.dto;

import lombok.Data;

@Data
public class UpdateUserInfoDto {

  private  String nickName;
  private  String intro;
  private String avatar;
  private String password;
  private String passwordCheck;
}
