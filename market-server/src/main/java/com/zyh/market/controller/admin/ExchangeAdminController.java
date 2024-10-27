package com.zyh.market.controller.admin;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.market.R;
import com.zyh.market.dto.PublicPageDto;
import com.zyh.market.service.UserExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/exchange")
@SaCheckLogin
@SaCheckRole(value = {"admin"})
public class ExchangeAdminController {

    @Autowired private UserExchangeService exchangeService;

    @GetMapping("/page")
    public R<Page> getExchangeList(PublicPageDto dto) {
        Page page = exchangeService.getExchangePage(dto);
        return R.ok(page);
    }
}
