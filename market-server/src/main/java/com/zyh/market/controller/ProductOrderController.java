package com.zyh.market.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.zyh.market.R;
import com.zyh.market.annotations.Idempotent;
import com.zyh.market.config.StpRole;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.dto.ProductOrderDto;
import com.zyh.market.dto.ProductOrderEvaluateDto;
import com.zyh.market.dto.ProductOrderPostDto;
import com.zyh.market.entity.ProductOrder;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.service.ProductOrderService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/product/order")
@SaCheckLogin
public class ProductOrderController {
  @Autowired private ProductOrderService productOrderService;

  @PostMapping
  @Idempotent(keyPrefix = "productOrder")
  public R<String> createOrder(@RequestBody ProductOrderDto dto){
    String id = productOrderService.createOrder(dto.getProductId(), dto.getInfo(), dto.getAddress(), dto.getIdName(), dto.getPhone());
    return R.ok(id);
  }
  @GetMapping("detail")
  public R<Map> getOrderDetail(String productOrderId){
   Map map = productOrderService.getOrderDetail(productOrderId);
   return R.ok(map);
  }
  @GetMapping
  public R<ProductOrder> getProductOrderInfo(String productOrderId){
    ProductOrder productOrder = productOrderService.getById(productOrderId);
    if(BeanUtil.isEmpty(productOrder)) throw new ServiceException(ResultCode.NotFindError);
    return R.ok(productOrder);
  }
  @GetMapping("/stat")
  public R<Map> getUserOrderStat(){
    HashMap<String,String> map = productOrderService.getUserOrderStat();
    return R.ok(map);
  }
  @GetMapping("/my/sell")
  public R<List<ProductOrder>> getMySellOrder(){
    List<ProductOrder> list =  productOrderService.getMySellOrder();
    return R.ok(list);
  }
  @GetMapping("/my/buy")
  public R<List<ProductOrder>> getMyBuyOrder() {
    List<ProductOrder> list = productOrderService.getMyBuyOrder();
    return R.ok(list);
  }
  

  @ApiOperation("用户提货")
  @PutMapping("/user/self/{productOrderId}")
  public R userSelf(@PathVariable("productOrderId") String productOrderId){
    boolean update = productOrderService.lambdaUpdate().eq(ProductOrder::getId, productOrderId)
      .eq(ProductOrder::getDealStatus, 4)
      .eq(ProductOrder::getPostMode, "用户自提")
      .set(ProductOrder::getDealStatus, 9).update();
    if(!update) throw new ServiceException(ResultCode.UpdateError);
    return R.ok();
  }
  
  @ApiOperation("物流发货")
  @PutMapping("/post")
  public R post(@RequestBody ProductOrderPostDto dto){
    boolean update = productOrderService.lambdaUpdate()
      .eq(ProductOrder::getId, dto.getProductOrderId())
      .eq(ProductOrder::getDealStatus, 3)
      .eq(ProductOrder::getPostMode, "物流发货")
      .set(ProductOrder::getDealStatus, 4)
      .set(ProductOrder::getShipCompany, dto.getPostCompany())
      .set(ProductOrder::getShipNum, dto.getPostNum())
      .set(ProductOrder::getShipTime, new Date()).update();
    if (!update) throw new ServiceException(ResultCode.UpdateError);
    return R.ok();
  }
  
  @ApiOperation("上传提货地址")
  @PutMapping("/selfPost")
  public R selfPost(@RequestBody ProductOrderPostDto dto) {
    boolean update = productOrderService.lambdaUpdate()
      .eq(ProductOrder::getId, dto.getProductOrderId())
      .eq(ProductOrder::getDealStatus, 3)
      .eq(ProductOrder::getPostMode, "用户自提")
      .set(ProductOrder::getDealStatus, 4)
      .set(ProductOrder::getShipUsername, dto.getUsername())
      .set(ProductOrder::getShipPhone, dto.getPhone())
      .set(ProductOrder::getShipAddress, dto.getAddress())
      .set(ProductOrder::getShipTime, new Date()).update();
    if (!update) throw new ServiceException(ResultCode.UpdateError);
    return R.ok();
  }
  @ApiOperation("确认收货")
  @PutMapping("/post/confirm/{productOrderId}")
  public R confirmPost(@PathVariable("productOrderId") String productOrderId){
    boolean update = productOrderService.lambdaUpdate()
      .eq(ProductOrder::getId, productOrderId)
      .eq(ProductOrder::getDealStatus, 4)
      .eq(ProductOrder::getPostMode, "物流发货")
      .set(ProductOrder::getDealStatus, 9)
      .set(ProductOrder::getShipTime, new Date()).update();
    if (!update) throw new ServiceException(ResultCode.UpdateError);
    return R.ok();
  }
  
  @ApiOperation("评价")
  @PutMapping("/evaluate")
  public R evaluate(@RequestBody ProductOrderEvaluateDto dto){
    productOrderService.lambdaUpdate().eq(ProductOrder::getId, dto.getId())
      .eq(ProductOrder::getUserId, StpUtil.getLoginIdAsString())
      .eq(ProductOrder::getDealStatus, 9)
      .set(ProductOrder::getEvaScore, dto.getScore())
      .set(ProductOrder::getEvaContent, dto.getContent())
      .set(ProductOrder::getDealStatus, 11)
      .set(ProductOrder::getUpdateTime, new Date()).update();
    return R.ok();
  }
}
