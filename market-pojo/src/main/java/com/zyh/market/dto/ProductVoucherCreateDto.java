package com.zyh.market.dto;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ProductVoucherCreateDto {
  
  @ApiModelProperty("")
  private String productId;
  
  @ApiModelProperty("")
  private String title;
  
  /**
   * 优惠金额
   */
  @ApiModelProperty(value = "优惠金额", example = "0")
  private Double voucherValue;

  private Long beginTime;

  private Long endTime;
  

}
