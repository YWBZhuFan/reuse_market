package com.zyh.market.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.zyh.market.entity.ChatMessage;
import com.zyh.market.entity.Comment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChatListVo {
  
  @ApiModelProperty("")
  @TableId(value = "id")
  private String id;
  
  @ApiModelProperty("")
  private String fromUserId;
  
  @ApiModelProperty("")
  private String toUserId;

  // 1对话框聊天 2回复 3评论
  private String type;

  @ApiModelProperty("")
  private String fromUserNick;
  private String fromUserAvatar;
  private String productId;
  private String productImage;
  @ApiModelProperty("")
  private String toUserNick;
  private String toUserAvatar;
  @ApiModelProperty(value = "", example = "0")
  private Long createTime;
  @ApiModelProperty(value = "", example = "0")
  private Long updateTime;
  private ChatMessage chatMessage;
  private Comment comment;
  private Integer noReadCount;
}
