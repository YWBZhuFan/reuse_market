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
@TableName("payment_pay")
public class PaymentPay implements Serializable {
  private static final long serialVersionUID = 1L;
  
    /**
   * 支付id
   */
  @ApiModelProperty("支付id")
  @TableId(value = "id")
  private String id;

    /**
   * 用户id
   */
  @ApiModelProperty("用户id")
  private String userId;

    /**
   * 订单UUID
   */
  @ApiModelProperty("订单UUID")
  private String orderId;

    /**
   * 支付金额
   */
  @ApiModelProperty(value ="支付金额" , example = "0")
  private Long paymentPrice;

    /**
   * 支付类型
   */
  @ApiModelProperty("支付类型")
  private String paymentType;

    /**
   * 支付返回数据
   */
  @ApiModelProperty("支付返回数据")
  private String paymentResultData;

    /**
   * 交易起始时间
   */
  @ApiModelProperty("交易起始时间")
  private String paymentTimeStart;

    /**
   * 交易结束时间
   */
  @ApiModelProperty("交易结束时间")
  private String paymentTimeExpire;

    /**
   * 支付状态，0待支付，1支付中，2支付失败,8已退款，9支付成功
   */
  @ApiModelProperty(value ="支付状态，0待支付，1支付中，2支付失败,8已退款，9支付成功" , example = "0")
  private Integer paymentStatus;

    /**
   * 支付状态处理、0不处理，1未处理、9已处理
   */
  @ApiModelProperty(value ="支付状态处理、0不处理，1未处理、9已处理" , example = "0")
  private Integer processStatus;

    /**
   * 创建时间
   */
  @ApiModelProperty("创建时间")
  private Date timeCreate;

    /**
   * 最后一次更新时间
   */
  @ApiModelProperty("最后一次更新时间")
  private Date timeUpdate;

    /**
   * 完成时间
   */
  @ApiModelProperty("完成时间")
  private Date timeFinish;



}