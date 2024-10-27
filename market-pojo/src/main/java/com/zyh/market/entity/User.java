package com.zyh.market.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

@Data
@Accessors(chain = true)
@ApiModel("")
@TableName("user")
@Builder
public class User implements Serializable {
  private static final long serialVersionUID = 1L;
  
    /**
   * 用户标识
   */
  @ApiModelProperty("用户标识")
  @TableId(value = "id")
  private String id;

    /**
   * 头像
   */
  @ApiModelProperty("头像")
  private String avatar;

    /**
   * 个人简介
   */
  @ApiModelProperty("个人简介")
  private String intro;

    /**
   * 昵称
   */
  @ApiModelProperty("昵称")
  private String nickName;

    /**
   * 手机号
   */
  @ApiModelProperty(value ="手机号" , example = "0")
  private Long phone;

    /**
   * 密码
   */
  @ApiModelProperty("密码")
  private String password;

    /**
   * 状态
   */
  @ApiModelProperty(value ="状态" , example = "0")
  private Integer status;

  /**
   * 用户积分
   */
  private Integer points;

  /**
   * 邀请人
   */
  private String inviterId;

  /**
   * 邀请码码
   */
  private String shareCode;
    /**
   * 创建时间
   */
  @ApiModelProperty(value ="创建时间" , example = "0")
  private Long updateTime;

    /**
   * 更新时间
   */
  @ApiModelProperty(value ="更新时间" , example = "0")
  private Long createTime;

    /**
   * 定位省
   */
  @ApiModelProperty("定位省")
  private String province;

    /**
   * 定位市
   */
  @ApiModelProperty("定位市")
  private String city;

    /**
   * 平台编号
   */
  @ApiModelProperty("平台编号")
  private String number;

    /**
   * 待审核昵称
   */
  @ApiModelProperty("待审核昵称")
  private String checkNickName;

    /**
   * 待审核简介
   */
  @ApiModelProperty("待审核简介")
  private String checkIntro;

    /**
   * 待审核头像
   */
  @ApiModelProperty("待审核头像")
  private String checkAvatar;

    /**
   * 审核状态 0待审核 7审核失败 9审核成功
   */
  @ApiModelProperty(value ="审核状态 0待审核 7审核失败 9审核成功" , example = "0")
  private Integer checkStatus;



}