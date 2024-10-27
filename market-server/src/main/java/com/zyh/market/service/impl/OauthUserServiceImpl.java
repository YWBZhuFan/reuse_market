package com.zyh.market.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.market.entity.OauthUser;
import com.zyh.market.mapper.OauthUserMapper;
import com.zyh.market.service.OauthUserService;
import org.springframework.stereotype.Service;

@Service
public class OauthUserServiceImpl extends ServiceImpl<OauthUserMapper, OauthUser> implements OauthUserService {
}
