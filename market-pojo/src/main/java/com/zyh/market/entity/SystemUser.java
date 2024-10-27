package com.zyh.market.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.*;

@Data
@Accessors(chain = true)
@ApiModel("")
@TableName("system_user")
public class SystemUser implements Serializable {
  private static final long serialVersionUID = 1L;
  
    /**
   * 管理用户标识
   */
  @ApiModelProperty("管理用户标识")
  @TableId(value = "id")
  private String id;

    /**
   * 用户名
   */
  @ApiModelProperty("用户名")
  private String username;

    /**
   * 密码
   */
  @ApiModelProperty("密码")
  private String password;

    /**
   * 姓名
   */
  @ApiModelProperty("姓名")
  private String name;

    /**
   * 角色标识
   */
  @ApiModelProperty("角色标识")
  private String roleId;

    /**
   * 角色代码
   */
  @ApiModelProperty("角色代码")
  private String roleCode;

    /**
   * 角色名称
   */
  @ApiModelProperty("角色名称")
  private String roleName;

    /**
   * 创建时间
   */
  @ApiModelProperty(value ="创建时间" , example = "0")
  private Long createTime;

    /**
   * 更新时间
   */
  @ApiModelProperty(value ="更新时间" , example = "0")
  private Long updateTime;



}