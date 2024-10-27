package com.zyh.market.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.market.entity.PointsDetail;
import com.zyh.market.mapper.PointsDetailMapper;
import com.zyh.market.service.PointsDetailService;
import org.springframework.stereotype.Service;

@Service
public class PointsDetailServiceImpl extends ServiceImpl<PointsDetailMapper, PointsDetail> implements PointsDetailService {
}
