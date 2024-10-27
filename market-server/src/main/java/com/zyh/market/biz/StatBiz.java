package com.zyh.market.biz;

import com.zyh.market.service.ProductInfoService;
import com.zyh.market.service.ProductOrderService;
import com.zyh.market.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class StatBiz {
  @Autowired private ProductInfoService productInfoService;
  @Autowired private ProductOrderService productOrderService;
  @Autowired private UserService userService;
  public Map getCountData() {
    HashMap<Object, Object> map = new HashMap<>();
    //统计本日新增商品数
    Long productTodayCount = productInfoService.getTodayCount();
    //统计本月新增商品数
    Long productMonthCount = productInfoService.getMonthCount();
    //统计本日订单数
    Long orderTodayCount = productOrderService.getTodayCount();
    //统计本月订单数
    Long orderMonthCount = productOrderService.getMonthCount();
    //统计本日销售额
    Long moneyToday = productOrderService.getTodayMoneyCount();
    //统计本月销售额
    Long moneyMonth = productOrderService.getMonthMoneyCount();
    map.put("productTodayCount", productTodayCount);
    map.put("productMonthCount", productMonthCount);
    map.put("orderTodayCount", orderTodayCount);
    map.put("orderMonthCount", orderMonthCount);
    map.put("moneyToday", moneyToday);
    map.put("moneyMonth", moneyMonth);
    return map;
  }
  
  public List<Map> getUserData() {
    //获取本周一新增和活跃用户数
    List<Map> list = userService.getUserStat();
    return list;
  }
  
  public List<Map> getTableData() {
    List<Map> list = productOrderService.getTableData();
    return list;
  }
  
  public List<Map<String, Object>> getVideoData() {
    List<Map<String, Object>> list = productOrderService.getVideoData();
    return list;
  }
  
  public Map<String, List<Map<String, Long>>> getOrderData() {
    Map<String, List<Map<String, Long>>>  list = productOrderService.getOrderData();
    return list;
  }
}
