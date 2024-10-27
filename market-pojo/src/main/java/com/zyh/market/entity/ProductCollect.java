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
@TableName("product_collect")
public class ProductCollect implements Serializable {
  private static final long serialVersionUID = 1L;
  
    /**
   * 收藏标识
   */
  @ApiModelProperty("收藏标识")
  @TableId(value = "id")
  private String id;

    /**
   * 用户标识
   */
  @ApiModelProperty("用户标识")
  private String userId;

    /**
   * 商品标识
   */
  @ApiModelProperty("商品标识")
  private String productId;

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