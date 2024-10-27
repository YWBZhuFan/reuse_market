package com.zyh.market.service;

import cn.hutool.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public interface SocialLoginStrategy {

    Map<String, String> getAccessToken(String code) throws IOException;

    JSONObject getUserInfo(String accessToken) throws IOException;

    String handleLogin(JSONObject userInfo);
}
