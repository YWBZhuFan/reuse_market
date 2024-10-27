package com.zyh.market.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.market.entity.Follow;
import com.zyh.market.entity.ProductLike;
import com.zyh.market.mapper.FollowMapper;
import com.zyh.market.mapper.ProductLikeMapper;
import com.zyh.market.service.FollowService;
import com.zyh.market.service.ProductLikeService;
import org.springframework.stereotype.Service;

@Service
public class ProductLikeServiceImpl extends ServiceImpl<ProductLikeMapper, ProductLike> implements ProductLikeService {
}
