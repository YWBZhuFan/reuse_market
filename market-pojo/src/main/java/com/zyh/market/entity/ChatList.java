package com.zyh.market.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.*;

@Data
@Accessors(chain = true)
@TableName("chat_list")
public class ChatList implements Serializable {
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
   * 商品图片
   */
  @ApiModelProperty("商品图片")
  private String productImage;

    /**
   * 评论者用户标识
   */
  @ApiModelProperty("评论者用户标识")
  private String fromUserId;

    /**
   * 评论者用户头像
   */
  @ApiModelProperty("评论者用户头像")
  private String fromUserAvatar;

    /**
   * 评论者用户昵称
   */
  @ApiModelProperty("评论者用户昵称")
  private String fromUserNick;

    /**
   * 被评论者用户标识
   */
  @ApiModelProperty("被评论者用户标识")
  private String toUserId;

    /**
   * 被评论者用户昵称
   */
  @ApiModelProperty("被评论者用户昵称")
  private String toUserNick;

    /**
   * 被评论者用户头像
   */
  @ApiModelProperty("被评论者用户头像")
  private String toUserAvatar;

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