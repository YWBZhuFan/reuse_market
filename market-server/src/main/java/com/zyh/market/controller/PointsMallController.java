package com.zyh.market.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.zyh.market.R;
import com.zyh.market.dto.ProductInfoPageDto;
import com.zyh.market.dto.UserExchangeAddressDto;
import com.zyh.market.entity.PointsDetail;
import com.zyh.market.entity.PointsMallProduct;
import com.zyh.market.entity.PointsMallType;
import com.zyh.market.entity.UserExchange;
import com.zyh.market.service.PointsDetailService;
import com.zyh.market.service.PointsMallProductService;
import com.zyh.market.service.PointsMallTypeService;
import com.zyh.market.service.UserExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.zyh.market.constants.MySqlConstants.CREATE_TIME;

@RestController
@SaCheckLogin
@RequestMapping("/pointsMall")
public class PointsMallController {

    @Autowired private PointsMallTypeService pointsMallTypeService;
    @Autowired private PointsMallProductService pointsMallProductService;
    @Autowired private UserExchangeService userExchangeService;
    @Autowired private PointsDetailService pointsDetailService;

    @GetMapping("/type")
    public R<List<PointsMallType>> getTypeList(){
        return pointsMallTypeService.getTypeList();
    }

    @GetMapping("/page")
    public R<List<PointsMallProduct>> getTypePageList(ProductInfoPageDto pageDto){
        return pointsMallProductService.getTypeList(pageDto);
    }

    /**
     * 兑换
     * @param productId
     * @return
     */
    @PostMapping("/exchange/{productId}")
    public R exchange(@PathVariable(value = "productId") Integer productId){
        return pointsMallProductService.exchange(productId);
    }

    @GetMapping("/exchange/my")
    public R<List<UserExchange>> getMyExchangeList(){
        return pointsMallProductService.getMyExchangeList();
    }

    /**
     * 上传兑换地址
     * @param userExchangeAddressDto
     * @return
     */
    @PostMapping("/exchange/uploadAddress")
    public R uploadExchangeAddress(@RequestBody UserExchangeAddressDto userExchangeAddressDto){
        return userExchangeService.uploadExchangeAddress(userExchangeAddressDto);
    }

    @GetMapping("/my/points")
    public R<Integer> getUserPoints(){
        return R.ok(pointsMallProductService.getUserPoints());
    }

    @GetMapping("/pointsDetail")
    public R<List<PointsDetail>> getPointsDetail() {
        String userId = StpUtil.getLoginIdAsString();
        List<PointsDetail> pointsDetailList = pointsDetailService.query().eq("user_id", userId).orderByDesc(CREATE_TIME).list();
        return R.ok(pointsDetailList);
    }
}
