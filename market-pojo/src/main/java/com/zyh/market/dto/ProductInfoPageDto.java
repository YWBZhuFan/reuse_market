package com.zyh.market.dto;


import com.zyh.market.Page;
import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @author zhangyihua
 * @version 1.0
 * @description TODO
 * @date 2024/3/3 18:21
 */
@Data
public class ProductInfoPageDto extends Page {
  private int pageNumber;
  private int pageSize;
  private String typeCode;
  private String key;
  private String status;
}
