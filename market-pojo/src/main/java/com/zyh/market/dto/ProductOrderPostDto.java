package com.zyh.market.dto;

import lombok.Data;

@Data
public class ProductOrderPostDto {
  private String productOrderId;
  private String postCompany;
  private String postNum;
  private String username;
  private String phone;
  private String address;
  
}
