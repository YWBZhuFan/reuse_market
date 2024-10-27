package com.zyh.market.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONObject;
import com.zyh.market.config.GithubConfig;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.zyh.market.constants.MySqlConstants.GITHUB_ID;
import static com.zyh.market.constants.MySqlConstants.ID;

@Service
public class GithubAuthServiceImpl implements SocialLoginStrategy {

    @Autowired private OauthUserService oauthUserService;
    @Autowired private GithubConfig githubConfig;

    @Override
    public Map<String, String> getAccessToken(String code) throws IOException {
        String url = "https://github.com/login/oauth/access_token?";
        String query = "client_id=" + githubConfig.getClientId() + "&" +
                "client_secret=" + githubConfig.getClientSecret() + "&" +
                "code=" + code;
        String fullUrl = url + query;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(fullUrl);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity());
        String accessToken = result.split("&")[0].split("=")[1];
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", accessToken);
        tokenMap.put("refreshToken", "refreshToken");
        return tokenMap;
    }

    @Override
    public JSONObject getUserInfo(String accessToken) throws IOException {
        String url = "https://api.github.com/user?access_token=" + accessToken;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Authorization", "token "+accessToken);
            httpGet.setHeader("X-GitHub-Api-Version", "2022-11-28");
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    String errorResponse = EntityUtils.toString(response.getEntity());
                    EntityUtils.consume(response.getEntity());
                    throw new IOException("Unexpected status code: " + statusCode + ", Error Response: " + errorResponse);
                }
                String result = EntityUtils.toString(response.getEntity());
                EntityUtils.consume(response.getEntity());
                return new JSONObject(result);
            }
        }
    }

    @Override
    public String handleLogin(JSONObject userInfo) {
        // 假设用户ID是唯一的标识
        String githubId = userInfo.getStr(ID);
        String userId = oauthUserService.query().eq(GITHUB_ID, githubId).one().getUserId();
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
