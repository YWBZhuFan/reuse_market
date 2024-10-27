package com.zyh.market.service;

import com.alipay.api.AlipayApiException;
import com.zyh.market.entity.AlipayBean;

public interface AliPayService {
    /**
     * 支付宝支付接口
     * @param alipayBean
     * @return
     * @throws AlipayApiException
     */
    String aliPay(AlipayBean alipayBean) throws AlipayApiException;
}
