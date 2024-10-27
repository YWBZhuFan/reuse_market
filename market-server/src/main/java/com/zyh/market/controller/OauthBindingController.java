package com.zyh.market.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.zyh.market.R;
import com.zyh.market.config.GiteeConfig;
import com.zyh.market.config.GithubConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SaCheckLogin
public class OauthBindingController {

    @Autowired
    private GithubConfig githubConfig;
    @Autowired private GiteeConfig giteeConfig;

    @GetMapping("/giteeBinding")
    public R giteeBinding(@RequestParam(value = "operation", defaultValue = "login") String operation) {
        String giteeUrl = "https://gitee.com/oauth/authorize?client_id=" + giteeConfig.getClientId() +
                "&redirect_uri=" + giteeConfig.getRedirectUri() + "&response_type=code" +
                "&state=" + operation+"-"+ StpUtil.getLoginIdAsString();
        return R.ok(giteeUrl);
    }

    @GetMapping("/githubBinding")
    public R githubLogin(@RequestParam(value = "operation", defaultValue = "login") String operation) {
        String githubUrl = "https://github.com/login/oauth/authorize?client_id="
                + githubConfig.getClientId() + "&redirect_uri=" +
                githubConfig.getRedirectUri() + "&response_type=code" + "&state=" + operation+"-"+ StpUtil.getLoginIdAsString();
        return R.ok(githubUrl);
    }
}
