package com.zyh.market.controller.admin;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.zyh.market.R;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.dto.ProductVoucherCreateDto;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.service.ProductVoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/product/voucher")
@SaCheckLogin
//@SaCheckRole(value = {"admin"})
public class ProductVoucherAdminController {
  @Autowired private ProductVoucherService productVoucherService;
  @PostMapping
  public R create(@RequestBody ProductVoucherCreateDto dto){
    productVoucherService.create(dto);
    return R.ok();
  }
  @DeleteMapping("/{id}")
  public R delete(@PathVariable("id") String id) {
    boolean update = productVoucherService.removeById(id);
    if(!update){
      throw new ServiceException(ResultCode.DeleteError);
    }
    return R.ok();
  }
}
