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
@TableName("product_type")
public class ProductType implements Serializable {
  private static final long serialVersionUID = 1L;
  
    /**
   * 类型标识
   */
  @ApiModelProperty("类型标识")
  @TableId(value = "id")
  private String id;

    /**
   * 类型代码
   */
  @ApiModelProperty("类型代码")
  private String typeCode;

    /**
   * 类型名称
   */
  @ApiModelProperty("类型名称")
  private String typeName;

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