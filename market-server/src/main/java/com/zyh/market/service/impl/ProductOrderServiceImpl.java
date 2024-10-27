package com.zyh.market.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.dto.SystemProductOrderPageDto;
import com.zyh.market.entity.*;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.mapper.ProductOrderMapper;
import com.zyh.market.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.zyh.market.constants.MySqlConstants.*;

@Service
public class ProductOrderServiceImpl extends ServiceImpl<ProductOrderMapper, ProductOrder> implements ProductOrderService {

  @Autowired private UserService userService;
  @Autowired private ProductInfoService productInfoService;
  @Autowired private PaymentOrderService paymentOrderService;
  @Autowired private ProductTypeService productTypeService;
  @Autowired private ProductCollectService productCollectService;
  @Autowired private RabbitTemplate rabbitTemplate;
  
  @Override
  @Transactional(rollbackFor = Exception.class)
  public String createOrder(String productId, String info, String address, String idName, String phone) {
    ProductOrder productOrder = new ProductOrder();
    String currentUserId = StpUtil.getLoginIdAsString();
    User user = userService.query().eq(ID, currentUserId).one();
    ProductInfo productInfo = productInfoService.getById(productId);
    if (BeanUtil.isEmpty(productInfo)){
      throw new ServiceException(ResultCode.BusinessError,"商品不存在");
    }
    if (productInfo.getStatus()!=9){
      throw new ServiceException(ResultCode.BusinessError,"商品已售出");
    }
    if (productInfo.getUserId().equals(currentUserId)){
      throw new ServiceException(ResultCode.BusinessError,"不能购买自己的商品");
    }
    if (productInfo.getPostType()==0){
      productOrder.setPostUsername(idName);
      productOrder.setPostAddress(address);
      productOrder.setPostPhone(phone);
    }else {
      productOrder.setPostAddress(address);
      productOrder.setPostPhone(phone);
    }
    productOrder.setPostMode("用户自提");
    String orderNum = createOrderNum();
    productOrder.setOrderNumber(orderNum);
    productOrder.setPostUsername(user.getNickName());
    productOrder.setProductId(productInfo.getId());
    productOrder.setProductUserId(productInfo.getUserId());
    productOrder.setUserId(StpUtil.getLoginIdAsString());
    productOrder.setProductType(productInfo.getTypeCode());
    productOrder.setProductTypeName(productInfo.getTypeName());
    productOrder.setProductTitle(productInfo.getTitle());
    productOrder.setProductImg(productInfo.getImage());
    productOrder.setProductPrice(productInfo.getOriginalPrice());
    productOrder.setProductSellPrice(productInfo.getPrice());
    productOrder.setProductNum(1);
    productOrder.setProductPost(0L);
    productOrder.setProductPostStatus(productInfo.getPostType());
    productOrder.setProductInfo(productInfo.getIntro());
    productOrder.setProductMoney(productOrder.getProductNum() * productOrder.getProductSellPrice());
    productOrder.setBuyMoney(productInfo.getPrice());
    productOrder.setBuyMoneyAll(productInfo.getPrice());
    productOrder.setBuyMoney(productInfo.getPrice()*productOrder.getProductNum());
    productOrder.setBuyInfo(info);
    productOrder.setDealStatus(0);
    productOrder.setCreateTime(new DateTime());
    productOrder.setUpdateTime(new DateTime());
    productOrder.setPayStatus(0);
    productInfo.setStatus(12);
    productInfoService.lambdaUpdate().eq(ProductInfo::getId, productInfo.getId()).set(ProductInfo::getStatus, 12).update();
    boolean isExists = productCollectService.query()
            .eq(USER_ID, currentUserId)
            .eq(PRODUCT_ID, productId).exists();
    //如果是收藏商品 取消收藏
    if (isExists){
      boolean isSuccess = productCollectService.lambdaUpdate()
              .eq(ProductCollect::getUserId, currentUserId)
              .eq(ProductCollect::getProductId, productId)
              .remove();
      if (!isSuccess) throw new ServiceException(ResultCode.UpdateError, "下单失败");
    }
    boolean save = save(productOrder);
    if (!save) throw new ServiceException(ResultCode.SaveError);
    rabbitTemplate.convertAndSend("delay.direct",
            "pay.timeout",
            productOrder.getId(),
            message -> {
            message.getMessageProperties().setDelay(900000);
            return message;
            });
    return productOrder.getId();
  }
  
  @Override
  public HashMap<String, String> getUserOrderStat() {
    HashMap<String, String> result = new HashMap<>();
    String userId = StpUtil.getLoginIdAsString();
    //1.买入
    List<ProductOrder> buyList = lambdaQuery().eq(ProductOrder::getUserId, userId).list();
    result.put("buy", String.valueOf(buyList.size()));
    //2.卖出
    List<ProductOrder> sellList = lambdaQuery().eq(ProductOrder::getProductUserId, userId).list();
    result.put("sell", String.valueOf(sellList.size()));
    //3.发布
    List<ProductInfo> publishList = productInfoService.lambdaQuery().eq(ProductInfo::getUserId, userId).list();
    result.put("publish", String.valueOf(publishList.size()));
    List<ProductInfo> myProductCollectInfo = productInfoService.getMyProductCollectInfo();
    result.put("collect", String.valueOf(myProductCollectInfo.size()));
    return result;
  }
  
  @Override
  public List<ProductOrder> getMySellOrder() {
    String userId = StpUtil.getLoginIdAsString();
    List<ProductOrder> list = lambdaQuery().eq(ProductOrder::getProductUserId, userId).orderByDesc(ProductOrder::getCreateTime).list();
    return list;
  }
  
  @Override
  public List<ProductOrder> getMyBuyOrder() {
    String userId = StpUtil.getLoginIdAsString();
    List<ProductOrder> list = lambdaQuery().eq(ProductOrder::getUserId, userId).orderByDesc(ProductOrder::getCreateTime).list();
    return list;
  }
  
  @Override
  public Page getProductOrderList(SystemProductOrderPageDto dto) {
    Page<ProductOrder> page = lambdaQuery()
      .like(StrUtil.isNotEmpty(dto.getKey()), ProductOrder::getOrderNumber, dto.getKey()).or()
      .like(StrUtil.isNotEmpty(dto.getKey()), ProductOrder::getProductInfo, dto.getKey())
      .orderByDesc(ProductOrder::getCreateTime)
      .page(new Page<>(dto.getPageNumber(), dto.getPageSize()));
    return page;
  }
  
  @Override
  public Map getOrderDetail(String productOrderId) {
    HashMap<Object, Object> map = new HashMap<>();
    ProductOrder productOrder = getById(productOrderId);
    if (BeanUtil.isEmpty(productOrder)) throw new ServiceException(ResultCode.NotFindError);
    map.put("productOrder", productOrder);
    ProductInfo productInfo = productInfoService.getById(productOrder.getProductId());
    map.put("productInfo", productInfo);
    PaymentOrder paymentOrder = paymentOrderService.getById(productOrder.getPayOrderId());
    map.put("paymentOrder", paymentOrder);
/*    PaymentPay paymentPay = paymentPayService.getById(paymentOrder.getPaymentPayId());
    map.put("paymentPay", paymentPay);*/
    return map;
  }
  
  @Override
  public Page getProductOrderEvaluateList(SystemProductOrderPageDto dto) {
    Page<ProductOrder> page = lambdaQuery()
      .like(StrUtil.isNotEmpty(dto.getKey()), ProductOrder::getOrderNumber, dto.getKey()).or()
      .like(StrUtil.isNotEmpty(dto.getKey()), ProductOrder::getProductInfo, dto.getKey())
      .eq(ProductOrder::getDealStatus, 11)
      .orderByDesc(ProductOrder::getCreateTime)
      .page(new Page<>(dto.getPageNumber(), dto.getPageSize()));
    return page;
  }
  
  @Override
  public Long getTodayCount() {
    LocalDateTime startDay = LocalDate.now().atStartOfDay();
    LocalDateTime endDay = LocalDateTime.now();
    Long count = lambdaQuery()
      .between(ProductOrder::getCreateTime, startDay, endDay)
      .count();
    return count;
  }
  
  @Override
  public Long getMonthCount() {
    LocalDateTime startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
    LocalDateTime endDay = LocalDateTime.now();
    Long count = lambdaQuery()
      .between(ProductOrder::getCreateTime, startOfMonth, endDay)
      .count();
    return count;
  }
  
  @Override
  public Long getTodayMoneyCount() {
    Long money = 0L;
    LocalDateTime startDay = LocalDate.now().atStartOfDay();
    LocalDateTime endDay = LocalDateTime.now();;
    
    List<ProductOrder> list = lambdaQuery().between(ProductOrder::getCreateTime, startDay, endDay).list();
    for (ProductOrder productOrder : list) {
      money += productOrder.getBuyMoneyAll();
    }
    return money;
  }
  
  @Override
  public Long getMonthMoneyCount() {
    Long money = 0L;

    LocalDateTime startTime = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
    
    LocalDateTime endTime = LocalDateTime.now();
    List<ProductOrder> list = lambdaQuery().between(ProductOrder::getCreateTime, startTime, endTime).list();
    for (ProductOrder productOrder : list) {
      money += productOrder.getBuyMoneyAll();
    }
    return money;
  }
  
  @Override
  public List<Map> getTableData() {
    List<Map> salesData = new ArrayList<>();
    List<ProductType> list = productTypeService.list();
    
    List<Map> typeList = new ArrayList<>();
    for (ProductType productType : list) {
      Map productCategor = new HashMap();
      productCategor.put("code", productType.getTypeCode());
      productCategor.put("name", productType.getTypeName());
      typeList.add(productCategor);
    }
    
    for (Map category : typeList) {
      Map<String, Object> categoryData = new HashMap<>();
      categoryData.put("type", category.get("name").toString());
      categoryData.put("todayBuy", __getTodaySalesForCategory(category.get("code").toString()));
      categoryData.put("monthBuy", __getMonthSalesForCategory(category.get("code").toString()));
      categoryData.put("totalBuy", __getTotalSalesForCategory(category.get("code").toString()));
      
      salesData.add(categoryData);
    }
    
    return salesData;
  }
  
  @Override
  public List<Map<String, Object>> getVideoData() {
    
    // 将结果转换为所需的格式
    List<Map<String, Object>> result = new ArrayList<>();
    // 获取所有订单
    List<ProductOrder> orders = list();
    //获取所有订单总销售额
    Long totalSales = orders.stream().mapToLong(ProductOrder::getBuyMoneyAll).sum();
    
    // 按商品类型进行分组，并计算每个类型的总销售额
    Map<String, Long> salesByProductType = orders.stream()
      .collect(Collectors.groupingBy(ProductOrder::getProductTypeName,
        Collectors.summingLong(ProductOrder::getBuyMoneyAll)));
    
    
    for (Map.Entry<String, Long> entry : salesByProductType.entrySet()) {
      Map<String, Object> item = new HashMap<>();
      item.put("name", entry.getKey());
      // 计算 entry.getValue() 与  totalSales 的比值 乘以 100，保留两位小数
      item.put("value", (double) Math.round(entry.getValue() * 10000 / totalSales) / 100 );
      result.add(item);
    }
    
    return result;
  }
  
  @Override
  public Map<String, List<Map<String, Long>>> getOrderData() {
    // 获取所有的商品分类
    List<String> allProductTypes = productTypeService.list().stream()
      .map(ProductType::getTypeName)
      .collect(Collectors.toList());

// 获取最近7天的开始和结束日期
    LocalDateTime startDay = LocalDate.now().minusDays(7).atStartOfDay();
    LocalDateTime endDay = LocalDateTime.now();

// 获取最近7天的所有订单
    List<ProductOrder> orders = lambdaQuery()
      .between(ProductOrder::getCreateTime, startDay, endDay)
      .list();

// 按日期和商品类型进行分组，并计算每个类型的每日销售量
    Map<String, Map<String, Long>> salesByDateAndProductType = orders.stream()
      .collect(Collectors.groupingBy(
        order -> {
          Date date = order.getCreateTime();
          LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
          return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));},
        Collectors.groupingBy(ProductOrder::getProductTypeName, Collectors.counting())
      ));

// 生成最近7天的日期列表
    List<String> lastSevenDays = IntStream.range(0, 7)
      .mapToObj(i -> LocalDate.now().minusDays(i).toString())
      .collect(Collectors.toList());

// 对每个日期和每个商品类型进行处理
    Map<String, List<Map<String, Long>>> result = new LinkedHashMap<>();
    
    
    for (String date : lastSevenDays) {
      Map<String, Long> salesByProductType = salesByDateAndProductType.getOrDefault(date, new HashMap<>());
      List<Map<String, Long>> salesData = new ArrayList<>();
      for (String productType : allProductTypes) {
        Map<String, Long> item = new HashMap<>();
        item.put(productType, salesByProductType.getOrDefault(productType, 0L));
        salesData.add(item);
      }
      result.put(date, salesData);
    }
    
    return result;
  }
  
  private Long __getTotalSalesForCategory(String category) {
    Long count = lambdaQuery()
      .eq(ProductOrder::getProductType, category)
      .count();
    return count;
  }
  
  private Long __getMonthSalesForCategory(String category) {
    LocalDateTime startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
    LocalDateTime endDay = LocalDateTime.now();
    Long count = lambdaQuery()
      .between(ProductOrder::getCreateTime, startOfMonth, endDay)
      .eq(ProductOrder::getProductType, category)
      .count();
    return count;
  }
  
  private Long __getTodaySalesForCategory(String category) {
    LocalDateTime startDay = LocalDate.now().atStartOfDay();
    LocalDateTime endDay = LocalDateTime.now();
    Long count = lambdaQuery()
      .between(ProductOrder::getCreateTime, startDay, endDay)
      .eq(ProductOrder::getProductType, category)
      .count();
    return count;
  }

  public String createOrderNum() {
    long time = System.currentTimeMillis();
    // 将毫秒时间戳转换为Instant对象
    Instant instant = Instant.ofEpochMilli(time);
    // 将Instant对象转换为LocalDateTime对象，使用系统默认时区
    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    // 创建一个DateTimeFormatter对象，指定输出格式
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    // 使用DateTimeFormatter对象格式化LocalDateTime
    String formattedDateTime = localDateTime.format(formatter);
    //生成5位随机数作为订单号前缀
    String timePrefix = RandomUtil.randomNumbers(5);
      return timePrefix + formattedDateTime;
  }
}
