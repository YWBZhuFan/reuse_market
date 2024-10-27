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
@TableName("payment_type")
public class PaymentType implements Serializable {
  private static final long serialVersionUID = 1L;
  
    /**
   * 类型标识
   */
  @ApiModelProperty(value ="类型标识" , example = "0")
  @TableId(value = "id")
  private Long id;

    /**
   * 类型名
   */
  @ApiModelProperty("类型名")
  private String typeName;

    /**
   * 1 可用 0 不可用
   */
  @ApiModelProperty(value ="1 可用 0 不可用" , example = "0")
  private Double wxPay;

    /**
   * 1 可用 0不可用
   */
  @ApiModelProperty(value ="1 可用 0不可用" , example = "0")
  private Double aliPay;



}