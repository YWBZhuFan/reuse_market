package com.zyh.market.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExcelModel {

    @ExcelProperty("日期")
    private String datetime;

    @ExcelProperty("手机数码")
    private String digital;

    @ExcelProperty("卡券")
    private String coupon;

    @ExcelProperty("闲置书籍")
    private String book;

    @ExcelProperty("生活用品")
    private String life;

    @ExcelProperty("日销售额")
    private String daySale;
}
