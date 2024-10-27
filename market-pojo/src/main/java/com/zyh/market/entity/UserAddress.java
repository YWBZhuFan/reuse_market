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
@TableName("user_address")
public class UserAddress implements Serializable {
  private static final long serialVersionUID = 1L;
  
    /**
   * 地址标识
   */
  @ApiModelProperty("地址标识")
  @TableId(value = "id")
  private String id;

    /**
   * 用户标识
   */
  @ApiModelProperty("用户标识")
  private String userId;

    /**
   * 姓名
   */
  @ApiModelProperty("姓名")
  private String name;

    /**
   * 手机号
   */
  @ApiModelProperty("手机号")
  private String phone;

    /**
   * 详细地址
   */
  @ApiModelProperty("详细地址")
  private String address;

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