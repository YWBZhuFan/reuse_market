package com.zyh.market.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("points_detail")
public class PointsDetail {
    private String id;
    private String userId;
    private Integer type;
    private String productTitle;
    private Integer pointsChange;
    private Integer pointsTotal;
    private LocalDateTime createTime;
}
