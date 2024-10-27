package com.zyh.market.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("oauth_user")
public class OauthUser {
    @TableId
    private String id;
    private String userId;
    private String giteeId;
    private String githubId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
