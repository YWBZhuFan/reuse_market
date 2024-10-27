package com.zyh.market.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangyihua
 * @version 1.0
 * @description TODO
 * @date 2024/2/24 17:19
 */
@Data
public class SystemUserCreateDto {
  

  @ApiModelProperty("用户名")
  private String username;
  

  @ApiModelProperty("密码")
  private String password;

  @ApiModelProperty("姓名")
  private String name;
  
  @ApiModelProperty("角色code")
  private String roleCode;

  
}
