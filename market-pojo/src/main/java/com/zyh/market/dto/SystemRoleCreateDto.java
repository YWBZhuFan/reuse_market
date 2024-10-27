package com.zyh.market.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangyihua
 * @version 1.0
 * @description TODO
 * @date 2024/2/22 21:25
 */
@Data
public class SystemRoleCreateDto {
  /**
   * 角色code
   */
  @ApiModelProperty("角色code")
  private String roleCode;
  
  /**
   * 角色名
   */
  @ApiModelProperty("角色名")
  private String roleName;
  
}
