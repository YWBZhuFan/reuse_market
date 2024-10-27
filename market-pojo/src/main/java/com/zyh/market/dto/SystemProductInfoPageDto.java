package com.zyh.market.dto;


import com.zyh.market.Page;
import lombok.Data;

@Data
public class SystemProductInfoPageDto extends Page {
  private String key;
  private String status;
}
