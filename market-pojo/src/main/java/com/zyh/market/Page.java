package com.zyh.market;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangyihua
 * @version 1.0
 * @description TODO
 * @date 2024/2/21 21:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Page {
  private int pageSize;
  private int pageNumber;
}
