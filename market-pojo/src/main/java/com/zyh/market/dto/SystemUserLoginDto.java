package com.zyh.market.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangyihua
 * @version 1.0
 * @description TODO
 * @date 2024/2/18 21:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemUserLoginDto {
  private String username;
  private String password;
}
