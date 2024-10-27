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
@TableName("voucher_order")
public class VoucherOrder implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @ApiModelProperty("")
  @TableId(value = "id")
  private String id;

  @ApiModelProperty("")
  private String userId;

  @ApiModelProperty("")
  private String productId;

  @ApiModelProperty("")
  private String voucherId;

  @ApiModelProperty(value ="" , example = "0")
  private Long voucherValue;

    /**
   * 0已使用 9 未使用
   */
  @ApiModelProperty(value ="0已使用 9 未使用" , example = "0")
  private Integer status;

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