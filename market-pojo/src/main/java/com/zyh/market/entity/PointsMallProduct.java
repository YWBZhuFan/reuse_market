package com.zyh.market.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName("pointsmall_product_info")
public class PointsMallProduct implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Integer id;

    private String title;

    private String image;

    private Integer price;

    private Integer status;

    @JsonIgnore
    private Integer stock;

    private Integer exchangeNumber;

    private Integer typeCode;

    private String typeName;

    private DateTime createTime;

    private DateTime updateTime;


}
