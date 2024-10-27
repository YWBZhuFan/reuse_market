package com.zyh.market.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Accessors(chain = true)
@ApiModel("")
@TableName("product_voucher")
public class ProductVoucher implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @ApiModelProperty("")
  @TableId(value = "id")
  private String id;

  @ApiModelProperty("")
  private String productId;

  @ApiModelProperty("")
  private String title;

    /**
   * 优惠金额
   */
  @ApiModelProperty(value ="优惠金额" , example = "0")
  private Long voucherValue;

  @ApiModelProperty(value ="" , example = "0")
  private Integer stock;

  @ApiModelProperty(value ="" , example = "0")
  private LocalDateTime beginTime;

  @ApiModelProperty(value ="" , example = "0")
  private LocalDateTime endTime;

    /**
   * 0不可见 9 可见
   */
  @ApiModelProperty(value ="0不可见 9 可见" , example = "0")
  private Integer voucherStatus;

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