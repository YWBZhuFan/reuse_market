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
@TableName("system_role")
public class SystemRole implements Serializable {
  private static final long serialVersionUID = 1L;
  
    /**
   * 角色标识
   */
  @ApiModelProperty("角色标识")
  @TableId(value = "id")
  private String id;

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