package com.zyh.market.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class FollowVo {
    @TableId
    private String id;
    private String userId;
    private String nickName;
    private String avatar;
    private Long createTime;
    private Integer isRead;
    private Integer noReadCount;
}
