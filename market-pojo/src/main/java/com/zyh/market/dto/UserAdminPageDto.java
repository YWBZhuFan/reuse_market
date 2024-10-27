package com.zyh.market.dto;


import com.zyh.market.Page;
import lombok.Data;

@Data
public class UserAdminPageDto extends Page {
  private String key;
  private Integer checkStatus;
}
