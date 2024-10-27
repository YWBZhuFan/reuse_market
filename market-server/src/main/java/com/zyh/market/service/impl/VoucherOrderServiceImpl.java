package com.zyh.market.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.entity.ProductVoucher;
import com.zyh.market.entity.VoucherOrder;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.mapper.VoucherOrderMapper;
import com.zyh.market.service.ProductVoucherService;
import com.zyh.market.service.VoucherOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Date;

import static com.zyh.market.constants.MySqlConstants.ID;
import static com.zyh.market.constants.MySqlConstants.STOCK;

@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements VoucherOrderService {
  @Autowired private ProductVoucherService productVoucherService;

  
  @Override
  public void seckill(String voucherId) {
    ProductVoucher seckillVoucher = productVoucherService.getById(voucherId);
    if (BeanUtil.isEmpty(seckillVoucher)) throw new ServiceException(ResultCode.NotFindError);

    if (seckillVoucher.getBeginTime().isAfter(LocalDateTime.now())) {
      throw new ServiceException(ResultCode.Fail, "秒杀尚未开始");
    }
    if (seckillVoucher.getEndTime().isBefore(LocalDateTime.now())) {
      throw new ServiceException(ResultCode.Fail, "秒杀已经结束");
    }
    //判断是否还有库存
    if (seckillVoucher.getStock() < 1) {
      throw new ServiceException(ResultCode.Fail, "库存不足");
    }
    String userId = StpUtil.getLoginIdAsString();
    synchronized (userId.intern()) {
      
      VoucherOrderService proxy = (VoucherOrderService) AopContext.currentProxy();
      proxy.createVoucherId(voucherId, userId);
    }
  }
  
  @Override
  public void createVoucherId(String voucherId, String userId) {
    Long count = lambdaQuery().eq(VoucherOrder::getVoucherId, voucherId).eq(VoucherOrder::getUserId, userId).count();
    if (count > 0) {
      throw new ServiceException(ResultCode.Fail, "抢购成功");
    }
    boolean success = productVoucherService.update().setSql("stock = stock -1")
      .eq(ID, voucherId).gt(STOCK, 0).update();
    if (!success) {
      throw new ServiceException(ResultCode.Fail, "库存不足");
    }
    ProductVoucher voucher = productVoucherService.getById(voucherId);
    //记录订单表
    VoucherOrder voucherOrder = new VoucherOrder();
    voucherOrder.setUserId(userId);
    voucherOrder.setVoucherId(voucherId);
    voucherOrder.setVoucherId(voucherId);
    voucherOrder.setProductId(voucher.getProductId());
    voucherOrder.setVoucherValue(voucher.getVoucherValue());
    voucherOrder.setStatus(9);
    voucherOrder.setCreateTime(new Date().getTime());
    voucherOrder.setUpdateTime(new Date().getTime());
    boolean save = save(voucherOrder);
    if (!save) {
      throw new ServiceException(ResultCode.SaveError, "抢购失败");
    }
  }
}
