package com.zyh.market.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Builder
@TableName("user_exchange")
public class UserExchange implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String userId;

    private String nickName;

    private Integer mallProductId;

    private Integer mallProductType;

    private Integer mallProductPrice;

    private String mallProductTitle;

    private String image;

    private Integer status;
    @TableField("post_name")
    private String name;
    @TableField("post_phone")
    private String phone;
    @TableField("post_address")
    private String address;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
