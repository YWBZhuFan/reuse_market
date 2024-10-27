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
@TableName("comment")
public class Comment implements Serializable {
  private static final long serialVersionUID = 1L;
  
    /**
   * 评论标识
   */
  @ApiModelProperty("评论标识")
  @TableId(value = "id")
  private String id;

    /**
   * 商品标识
   */
  @ApiModelProperty("商品标识")
  private String productId;

    /**
   * 父级评论标识
   */
  @ApiModelProperty("父级评论标识")
  private String parentId;

    /**
   * 发布者标识
   */
  @ApiModelProperty("发布者标识")
  private String pubUserId;

    /**
   * 发布者昵称
   */
  @ApiModelProperty("发布者昵称")
  private String pubNickname;

    /**
   * 父级用户标识
   */
  @ApiModelProperty("父级用户标识")
  private String parentUserId;

    /**
   * 父级用户昵称
   */
  @ApiModelProperty("父级用户昵称")
  private String parentUserNickname;

    /**
   * 评论内容
   */
  @ApiModelProperty("评论内容")
  private String content;

    /**
   * 创建时间
   */
  @ApiModelProperty(value ="创建时间" , example = "0")
  private Long createTime;

  private Integer isRead;


}