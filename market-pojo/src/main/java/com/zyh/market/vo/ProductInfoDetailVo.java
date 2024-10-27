package com.zyh.market.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.zyh.market.entity.ProductVoucher;
import com.zyh.market.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProductInfoDetailVo {
  
  @ApiModelProperty("")
  private String id;
  
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
  
  /**
   * 状态
   */
  @ApiModelProperty(value = "状态", example = "0")
  private Integer status;
  
  /**
   * 用户id
   */
  @ApiModelProperty("用户id")
  private String userId;
  
  /**
   * 创建时间
   */
  @ApiModelProperty(value = "创建时间", example = "0")
  private Long createTime;
  private User userInfo;
  
  /**
   * 更新时间
   */
  @ApiModelProperty(value = "更新时间", example = "0")
  private Long updateTime;
  private ProductVoucher productVoucher;
}
