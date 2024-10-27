package com.zyh.market.controller.admin;

import com.zyh.market.mapper.ProductOrderMapper;
import com.zyh.market.service.ProductOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExcelController {

    @Autowired
    private ProductOrderMapper productOrderMapper;

    @GetMapping("/admin/excel")
    public void createExcel() {

    }

}
