package com.zyh.market.dto;

import lombok.Data;

@Data
public class ChatListCreateDto {
  private String fromUserId;
  private String toUserId;
  private String productId;
  private String productImage;
}
