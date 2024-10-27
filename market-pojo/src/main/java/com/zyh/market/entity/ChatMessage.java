package com.zyh.market.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.springframework.data.annotation.Id;

import java.util.*;

@Data
@Accessors(chain = true)
@ApiModel("")
@TableName("chat_message")
public class ChatMessage implements Serializable {
  private static final long serialVersionUID = 1L;
  
    /**
   * 聊天信息标识
   */
  @ApiModelProperty("聊天信息标识")
  @TableId(value = "id")
  private String id;

    /**
   * 聊天记录标识
   */
  @ApiModelProperty("聊天记录标识")
  private String chatListId;

    /**
   * 发起者标识
   */
  @ApiModelProperty("发起者标识")
  private String fromUserId;

    /**
   * 接受者标识
   */
  @ApiModelProperty("接受者标识")
  private String toUserId;

    /**
   * 发起者昵称
   */
  @ApiModelProperty("发起者昵称")
  private String fromUserNick;

    /**
   * 接受者昵称
   */
  @ApiModelProperty("接受者昵称")
  private String toUserNick;

    /**
   * 聊天内容
   */
  @ApiModelProperty("聊天内容")
  private String content;

    /**
   * 发送时间
   */
  @ApiModelProperty(value ="发送时间" , example = "0")
  private Long sendTime;

    /**
   * 1已读0未读
   */
  @ApiModelProperty(value ="1已读0未读" , example = "0")
  private Integer isRead;



}