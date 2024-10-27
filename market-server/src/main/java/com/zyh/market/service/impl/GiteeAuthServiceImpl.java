package com.zyh.market.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zyh.market.config.GiteeConfig;
import com.zyh.market.service.OauthUserService;
import com.zyh.market.service.SocialLoginStrategy;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.zyh.market.constants.MySqlConstants.GITEE_ID;
import static com.zyh.market.constants.MySqlConstants.ID;

@Service
public class GiteeAuthServiceImpl implements SocialLoginStrategy {

    @Autowired private OauthUserService oauthUserService;
    @Autowired private GiteeConfig giteeConfig;

    public Map<String, String> getAccessToken(String code) throws IOException {
        String url = "https://gitee.com/oauth/token?";
        String query = "grant_type=authorization_code&" +
                "client_id=" + giteeConfig.getClientId() + "&" +
                "client_secret=" + giteeConfig.getClientSecret() + "&" +
                "redirect_uri=" + giteeConfig.getRedirectUri() + "&" +
                "code=" + code;
        String fullUrl = url + query;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(fullUrl);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity());
        EntityUtils.consume(response.getEntity());
        response.close();
        JSONObject json = JSONUtil.parseObj(result);
        String refreshToken = json.getStr("refresh_token");
        String accessToken = json.getStr("access_token");
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", accessToken);
        tokenMap.put("refreshToken", refreshToken);
        return tokenMap;
    }

    public JSONObject getUserInfo(String accessToken) throws IOException {
        String url = "https://gitee.com/api/v5/user?access_token=" + accessToken;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String result = EntityUtils.toString(response.getEntity());
        EntityUtils.consume(response.getEntity());
        response.close();
        return new JSONObject(result);
    }

    public String handleLogin(JSONObject userInfo) {
        // 假设用户ID是唯一的标识
        String giteeId = userInfo.getStr(ID);
        String userId = oauthUserService.query().eq(GITEE_ID, giteeId).one().getUserId();
        if (userId == null) {
            // 用户不存在，需要注册
            throw new RuntimeException("请先绑定账号！");
        }
        StpUtil.kickout(userId);
        // 在sa-token中登录用户
        StpUtil.login(userId);
        return StpUtil.getTokenValue();
    }

}
