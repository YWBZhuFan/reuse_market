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
@ApiModel("订单表")
@TableName("payment_order")
public class PaymentOrder implements Serializable {
  private static final long serialVersionUID = 1L;
  
    /**
   * 订单id
   */
  @ApiModelProperty("订单id")
  @TableId(value = "id")
  private String id;

    /**
   * 订单编号
   */
  @ApiModelProperty("订单编号")
  private String orderNumber;

    /**
   * 订单所属的用户
   */
  @ApiModelProperty("订单所属的用户")
  private String userId;

    /**
   * 总价
   */
  @ApiModelProperty(value ="总价" , example = "0")
  private Long payPrice;

    /**
   * 支付类型
   */
  @ApiModelProperty(value ="支付类型" , example = "0")
  private Long payTypeId;

    /**
   * 支付标题
   */
  @ApiModelProperty("支付标题")
  private String payTypeName;

    /**
   * 状态，0待生成付款，1待付款，2已付款，3付款失败，8关闭订单，9完成
   */
  @ApiModelProperty(value ="状态，0待生成付款，1待付款，2已付款，3付款失败，8关闭订单，9完成" , example = "0")
  private Integer orderStatus;

    /**
   * 支付id
   */
  @ApiModelProperty("支付id")
  private String paymentPayId;

    /**
   * 0待付款，1付款中，2付款失败，9付款成功
   */
  @ApiModelProperty(value ="0待付款，1付款中，2付款失败，9付款成功" , example = "0")
  private Integer paymentStatus;

    /**
   * 支付方式
   */
  @ApiModelProperty("支付方式")
  private String paymentType;

    /**
   * 状态处理、0不处理、1未处理、9已处理
   */
  @ApiModelProperty(value ="状态处理、0不处理、1未处理、9已处理" , example = "0")
  private Integer processStatus;

    /**
   * 创建时间
   */
  @ApiModelProperty("创建时间")
  private Date timeCreate;

    /**
   * 更新时间
   */
  @ApiModelProperty("更新时间")
  private Date timeUpdate;

    /**
   * 结束时间
   */
  @ApiModelProperty("结束时间")
  private Date timeFinish;
}