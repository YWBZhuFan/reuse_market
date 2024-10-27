package com.zyh.market.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.market.dto.SystemProductOrderPageDto;
import com.zyh.market.entity.ProductOrder;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ProductOrderService extends IService<ProductOrder> {
  String createOrder(String productId, String info, String address, String idname, String phone);

  
  HashMap<String, String> getUserOrderStat();
  
  
  List<ProductOrder> getMySellOrder();
  
  
  List<ProductOrder> getMyBuyOrder();
  
  
  Page getProductOrderList(SystemProductOrderPageDto dto);
  
  Map getOrderDetail(String productOrderId);
  
  
  Page getProductOrderEvaluateList(SystemProductOrderPageDto dto);
  
  Long getTodayCount();
  
  
  Long getMonthCount();
  
  
  Long getTodayMoneyCount();
  
  Long getMonthMoneyCount();
  
  List<Map> getTableData();
  
  List<Map<String, Object>> getVideoData();
  
  Map<String, List<Map<String, Long>>> getOrderData();
  
}
