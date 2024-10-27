package com.zyh.market.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangyihua
 * @version 1.0
 * @description TODO
 * @date 2024/3/3 14:47
 */
@Data
public class ProductInfoDto {
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
  private Double price;
  
  /**
   * 商品原价
   */
  @ApiModelProperty(value = "商品原价", example = "0")
  private Double originalPrice;
  
  /**
   * 发货方式 0邮寄 1自提
   */
  @ApiModelProperty(value = "发货方式 0邮寄 1自提", example = "0")
  private Integer postType;
  
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
  private String type;
}
