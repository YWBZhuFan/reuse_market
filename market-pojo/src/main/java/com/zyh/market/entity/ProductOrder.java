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
@ApiModel("商品订单表")
@TableName("product_order")
public class ProductOrder implements Serializable {
  private static final long serialVersionUID = 1L;
  
    /**
   * 订单标识
   */
  @ApiModelProperty("订单标识")
  @TableId(value = "id")
  private String id;

    /**
   * 订单编号
   */
  @ApiModelProperty("订单编号")
  private String orderNumber;

    /**
   * 商品发布用户标识
   */
  @ApiModelProperty("商品发布用户标识")
  private String productUserId;

    /**
   * 商品标识
   */
  @ApiModelProperty("商品标识")
  private String productId;

    /**
   * 买入 用户标识
   */
  @ApiModelProperty("买入 用户标识")
  private String userId;

    /**
   * 商品标题
   */
  @ApiModelProperty("商品标题")
  private String productTitle;

    /**
   * 商品图片
   */
  @ApiModelProperty("商品图片")
  private String productImg;

    /**
   * 商品原价
   */
  @ApiModelProperty(value ="商品原价" , example = "0")
  private Long productPrice;

    /**
   * 商品分类
   */
  @ApiModelProperty("商品分类")
  private String productType;

    /**
   * 商品分类名称
   */
  @ApiModelProperty("商品分类名称")
  private String productTypeName;

    /**
   * 商品售价
   */
  @ApiModelProperty(value ="商品售价" , example = "0")
  private Long productSellPrice;

    /**
   * 购买数量
   */
  @ApiModelProperty(value ="购买数量" , example = "0")
  private Integer productNum;

    /**
   * 快递费用
   */
  @ApiModelProperty(value ="快递费用" , example = "0")
  private Long productPost;

    /**
   * 发货方式
   */
  @ApiModelProperty(value ="发货方式" , example = "0")
  private Integer productPostStatus;

    /**
   * 购入商品价格
   */
  @ApiModelProperty(value ="购入商品价格" , example = "0")
  private Long productMoney;

    /**
   * 商品描述
   */
  @ApiModelProperty("商品描述")
  private String productInfo;

    /**
   * 应支付金额
   */
  @ApiModelProperty(value ="应支付金额" , example = "0")
  private Long buyMoneyAll;

    /**
   * 实际支付金额
   */
  @ApiModelProperty(value ="实际支付金额" , example = "0")
  private Long buyMoney;

    /**
   * 客户备注
   */
  @ApiModelProperty("客户备注")
  private String buyInfo;

    /**
   * 物流发货，学校领取，用户自提
   */
  @ApiModelProperty("物流发货，学校领取，用户自提")
  private String postMode;

    /**
   * 自提码
   */
  @ApiModelProperty("自提码")
  private String postSelfCode;

    /**
   * 收货人姓名
   */
  @ApiModelProperty("收货人姓名")
  private String postUsername;

    /**
   * 收货人联系方式
   */
  @ApiModelProperty("收货人联系方式")
  private String postPhone;

    /**
   * 收货地址
   */
  @ApiModelProperty("收货地址")
  private String postAddress;

    /**
   * 发货人姓名
   */
  @ApiModelProperty("发货人姓名")
  private String shipUsername;

    /**
   * 发货人联系方式
   */
  @ApiModelProperty("发货人联系方式")
  private String shipPhone;

    /**
   * 发货人地址
   */
  @ApiModelProperty("发货人地址")
  private String shipAddress;

    /**
   * 物流订单号码
   */
  @ApiModelProperty("物流订单号码")
  private String shipNum;

    /**
   * 物流公司
   */
  @ApiModelProperty("物流公司")
  private String shipCompany;

    /**
   * 发货时间
   */
  @ApiModelProperty("发货时间")
  private Date shipTime;

    /**
   * 0待支付，1支付中，9支付完成
   */
  @ApiModelProperty(value ="0待支付，1支付中，9支付完成" , example = "0")
  private Integer payStatus;

    /**
   * 支付订单id
   */
  @ApiModelProperty("支付订单id")
  private String payOrderId;

    /**
   * 交易状态，0临时，1交易失败，2，付款中，3，待发货，4待确认，7退货中，9待评价 11评价完成
   */
  @ApiModelProperty(value ="交易状态，0临时，1交易失败，2，付款中，3，待发货，4待确认，7退货中，9待评价 11评价完成" , example = "0")
  private Integer dealStatus;

    /**
   * 评价打分
   */
  @ApiModelProperty(value ="评价打分" , example = "0")
  private Integer evaScore;

    /**
   * 评价内容
   */
  @ApiModelProperty("评价内容")
  private String evaContent;

    /**
   * 创建时间
   */
  @ApiModelProperty("创建时间")
  private Date createTime;

    /**
   * 更新时间
   */
  @ApiModelProperty("更新时间")
  private Date updateTime;



}