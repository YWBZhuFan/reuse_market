package com.zyh.market.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName("pointsmall_type")
public class PointsMallType implements Serializable {
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