package com.zyh.market.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProductInfoTrendVo {
  private String id;
  
  /**
   * 发布用户id
   */
  @ApiModelProperty("发布用户id")
  private String userId;

  private String nickName;
  /**
   * 商品标题
   */
  @ApiModelProperty("商品标题")
  private String title;
  
  /**
   * 商品描述
   */
  @ApiModelProperty("商品描述")
  private String intro;
  
  /**
   * 商品图片
   */
  @ApiModelProperty("商品图片")
  private String image;
  
  /**
   * 商品价格
   */
  @ApiModelProperty(value = "商品价格", example = "0")
  private Long price;
  
  /**
   * 商品原价
   */
  @ApiModelProperty(value = "商品原价", example = "0")
  private Long originalPrice;
  
  /**
   * 种类代码
   */
  @ApiModelProperty("种类代码")
  private String typeCode;
  
  /**
   * 种类名称
   */
  @ApiModelProperty("种类名称")
  private String typeName;
  
  /**
   * 发货方式 0邮寄 1自提
   */
  @ApiModelProperty(value = "发货方式 0邮寄 1自提", example = "0")
  private Integer postType;
  
  /**
   * 想要的人数
   */
  @ApiModelProperty(value = "想要的人数", example = "0")
  private Integer likeCount;
  
  /**
   * 地址代码
   */
  @ApiModelProperty("地址代码")
  private String adcode;
  
  /**
   * 省
   */
  @ApiModelProperty("省")
  private String province;
  
  /**
   * 市
   */
  @ApiModelProperty("市")
  private String city;
  
  /**
   * 区
   */
  @ApiModelProperty("区")
  private String district;
  
  /**
   * 状态 9 上线 12 卖出
   */
  @ApiModelProperty(value = "状态 9 上线 12 卖出", example = "0")
  private Integer status;
  
  /**
   * 创建时间
   */
  @ApiModelProperty(value = "创建时间", example = "0")
  private Long createTime;
  
  /**
   * 更新时间
   */
  @ApiModelProperty(value = "更新时间", example = "0")
  private Long updateTime;
  private String type;
}
