package com.zyh.market.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.zyh.market.R;
import com.zyh.market.annotations.RequestLimit;
import com.zyh.market.config.GiteeConfig;
import com.zyh.market.config.GithubConfig;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.dto.UserLoginDto;
import com.zyh.market.dto.UserRegisterDto;
import com.zyh.market.entity.OauthUser;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.service.OauthUserService;
import com.zyh.market.service.SocialLoginStrategy;
import com.zyh.market.service.UserService;
import com.zyh.market.service.impl.SocialLoginContext;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.zyh.market.constants.Constants.HtmlCallbackUrlConstants.*;
import static com.zyh.market.constants.Constants.OAuthLoginTypeConstants.*;
import static com.zyh.market.constants.MySqlConstants.*;
import static com.zyh.market.constants.RedisConstants.RedissonClient.ProductId_Bloom_Filter;

/**
 * @author zhangyihua
 * @version 1.0
 * @description TODO
 * @date 2024/2/19 21:00
 */
@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

  @Autowired private UserService userService;
  @Autowired private RedissonClient redissonClient;
  @Autowired private GiteeConfig giteeConfig;
  @Autowired private GithubConfig githubConfig;
  @Autowired private OauthUserService oauthUserService;
  @Autowired private SocialLoginContext socialLoginContext;
  private static String webToken = "";
  private static String bindingType = "";

  @RequestLimit(permitsPerSecond = 1)
  @GetMapping("/getMessage")
  public R getMessage() {
    return R.ok("hello world");
  }

  /**
   * 注册
   * @param userRegisterDto
   * @return
   */
  @PostMapping("/user/register")
  public R register(@RequestBody UserRegisterDto userRegisterDto) {
    boolean isExist = validatePhoneNumber(userRegisterDto.getPhone());
    if (!isExist){throw new ServiceException(ResultCode.Fail,"请输入正确的手机号");}
    return userService.userRegister(userRegisterDto);
  }

  @GetMapping("/gitLogin")
  public R giteeLogin(@RequestParam(value = "operation", defaultValue = "login") String operation) {
    String giteeUrl = "https://gitee.com/oauth/authorize?client_id=" + giteeConfig.getClientId() +
            "&redirect_uri=" + giteeConfig.getRedirectUri() + "&response_type=code" +
            "&state=" + operation;
    return R.ok(giteeUrl);
  }

  @GetMapping("/githubLogin")
  public R githubLogin(@RequestParam(value = "operation", defaultValue = "login") String operation) {
    String githubUrl = "https://github.com/login/oauth/authorize?client_id="
            + githubConfig.getClientId() + "&redirect_uri=" +
            githubConfig.getRedirectUri() + "&response_type=code" + "&state=" + operation;
    return R.ok(githubUrl);
  }

  @GetMapping("/giteeCallback")
  public ResponseEntity<Void> giteeCallback(HttpServletRequest request) {
      String code = request.getParameter("code");
      String s = request.getParameter("state");
      String operation = s.split("-")[0];
      SocialLoginStrategy strategy = socialLoginContext.getStrategy(GITEE);
      if (strategy == null) {
          return ResponseEntity.status(302).location(URI.create(CALLBACK_ERROR_URL)).build();
      }
      String token;
      if (code != null && !code.isEmpty()) {
          try {
              //通过授权码换取访问令牌
              Map<String, String> tokenMap = strategy.getAccessToken(code);
              //使用访问令牌获取用户信息
              String accessToken = tokenMap.get("accessToken");
              String refreshToken = tokenMap.get("refreshToken");
              JSONObject userInfo = strategy.getUserInfo(accessToken);
              //绑定账号
              if (BINDING.equals(operation)){
                  String userId = s.split("-")[1];
                  bindingType = bindingGitee(userInfo,userId);
                  return ResponseEntity.status(302).location(URI.create(BINDING_SUCCESS_URL)).build();
              }
              boolean exists = oauthUserService.query().eq(GITEE_ID, userInfo.getStr(ID)).exists();
              if (!exists) {
                  webToken = NO_BINDING;
                  return ResponseEntity.status(302).location(URI.create(CALLBACK_SUCCESS_URL)).build();
              }
              //处理用户登录/,注册
              token = strategy.handleLogin(userInfo);
          } catch (Exception e) {
              e.printStackTrace();
              return null;
          }
      } else {
          return null;
          // 参数缺失处理逻辑
      }
      if (StrUtil.isEmpty(token)) {
          log.info("获取token失败");
          return ResponseEntity.status(302).location(URI.create(CALLBACK_ERROR_URL)).build();
      }
      webToken = token;
      return ResponseEntity.status(302).location(URI.create(CALLBACK_SUCCESS_URL)).build();
  }

    private String bindingGitee(JSONObject userInfo, String userId) {
        try {
            String giteeId = userInfo.getStr(ID);
            if (StrUtil.isEmpty(giteeId)){
                return ERROR;
            }
            OauthUser oauthUser = oauthUserService.query().eq(USER_ID, userId).one();
            if (BeanUtil.isEmpty(oauthUser)){
                boolean isExist = oauthUserService.query().eq(GITEE_ID, giteeId).exists();
                if (isExist){
                    return OTHER_BINDING_GITEE;
                }
                oauthUserService.save(new OauthUser().setUserId(userId).setGiteeId(giteeId).setCreateTime(LocalDateTime.now()));
                return BINDING_SUCCESS;
            }else {
                OauthUser oauthUser1 = oauthUserService.query().eq(GITEE_ID, giteeId).one();
                if (BeanUtil.isEmpty(oauthUser1)){
                    oauthUserService.update().set(GITEE_ID, giteeId).set(UPDATE_TIME, LocalDateTime.now()).eq(USER_ID, userId).update();
                    return BINDING_SUCCESS;
                }
                if (!BeanUtil.isEmpty(oauthUser1)){
                    if (oauthUser1.getUserId().equals(userId)){
                        return ALREADY_BINDING;
                    }
                    oauthUserService.update().set(GITEE_ID, giteeId).set(UPDATE_TIME, LocalDateTime.now()).eq(USER_ID, userId).update();
                    return BINDING_SUCCESS;
                } else if (!oauthUser1.getUserId().equals(userId)){
                    return OTHER_BINDING_GITEE;
                }
            }
        } catch (Exception e) {
            return ERROR;
        }
        return ERROR;
    }

    @GetMapping("/githubCallback")
    public ResponseEntity<Void> githubCallback(HttpServletRequest request) {
      String code = request.getParameter("code");
      String s = request.getParameter("state");
      String operation = s.split("-")[0];
      SocialLoginStrategy strategy = socialLoginContext.getStrategy(GITHUB);
      if (strategy == null) {
          return ResponseEntity.status(302).location(URI.create(CALLBACK_ERROR_URL)).build();
      }
      String token;
      if (code != null && !code.isEmpty()) {
          try {
              //通过授权码换取访问令牌
              Map<String, String> tokenMap = strategy.getAccessToken(code);
              //使用访问令牌获取用户信息
              String accessToken = tokenMap.get("accessToken");
              String refreshToken = tokenMap.get("refreshToken");
              JSONObject userInfo = strategy.getUserInfo(accessToken);
              if (BINDING.equals(operation)){
                  String userId = s.split("-")[1];
                  bindingType = bindingGithub(userInfo, userId);
                  return ResponseEntity.status(302).location(URI.create(BINDING_SUCCESS_URL)).build();
              }
              //处理用户登录/注册
              token = strategy.handleLogin(userInfo);
          } catch (Exception e) {
              e.printStackTrace();
              return null;
          }
      } else {
          return null;
          // 参数缺失处理逻辑
      }
      if (StrUtil.isEmpty(token)) {
          log.info("获取token失败");
          return ResponseEntity.status(302).location(URI.create(CALLBACK_ERROR_URL)).build();
      }
      webToken = token;
      return ResponseEntity.status(302).location(URI.create(CALLBACK_SUCCESS_URL)).build();
  }

    private String bindingGithub(JSONObject userInfo, String userId) {
        try {
            String githubId = userInfo.getStr(ID);
            if (StrUtil.isEmpty(githubId)){
                return ERROR;
            }
            OauthUser oauthUser = oauthUserService.query().eq(USER_ID, userId).one();
            if (BeanUtil.isEmpty(oauthUser)){
                boolean isExist = oauthUserService.query().eq(GITHUB_ID, githubId).exists();
                if (isExist){
                    return OTHER_BINDING_GITEE;
                }
                oauthUserService.save(new OauthUser().setUserId(userId).setGithubId(githubId).setCreateTime(LocalDateTime.now()));
                return BINDING_SUCCESS;
            }else {
                OauthUser oauthUser1 = oauthUserService.query().eq(GITHUB_ID, githubId).one();
                if (BeanUtil.isEmpty(oauthUser1)){
                    oauthUserService.update().set(GITHUB_ID, githubId).set(UPDATE_TIME, LocalDateTime.now()).eq(USER_ID, userId).update();
                    return BINDING_SUCCESS;
                }
                if (!BeanUtil.isEmpty(oauthUser1)){
                    if (oauthUser1.getUserId().equals(userId)){
                        return ALREADY_BINDING;
                    }
                    oauthUserService.update().set(GITHUB_ID, githubId).set(UPDATE_TIME, LocalDateTime.now()).eq(USER_ID, userId).update();
                    return BINDING_SUCCESS;
                } else if (!oauthUser1.getUserId().equals(userId)){
                    return OTHER_BINDING_GITEE;
                }
            }
        } catch (Exception e) {
            return ERROR;
        }
        return ERROR;
    }

  @GetMapping("/getGitCallback")
  public R getGitCallback() {
    if (StrUtil.isEmpty(webToken)){
      return R.fail(ResultCode.Fail,"获取token失败");
    }
    String token = webToken;
    webToken = "";
    return R.ok(token);
  }

  @GetMapping("/getBindingStatus")
  public R getBindingStatus() {
      String status = bindingType;
      bindingType = "";
      return R.ok(status);
  }

  /**
   * 登录
   * @param request
   * @return
   */
  @PostMapping("/user/login")
  public R<SaTokenInfo> login(@RequestBody UserLoginDto request) {
    boolean isExist = validatePhoneNumber(request.getPhone());
    if (!isExist){throw new ServiceException(ResultCode.Fail,"请输入正确的手机号");}
    SaTokenInfo loginToken = userService.login(request);
    return R.ok(loginToken);
  }

  /**
   * 密码登录
   * @param request
   * @return
   */
  @PostMapping("/user/login/pwd")
  public R<SaTokenInfo> loginPwd(@RequestBody UserLoginDto request) {
    boolean isExist = validatePhoneNumber(request.getPhone());
    if (!isExist){throw new ServiceException(ResultCode.Fail,"请输入正确的手机号");}
    SaTokenInfo loginToken = userService.loginPwd(request);
    return R.ok(loginToken);
  }

  /**
   * 获取验证码
   *
   * @param phone
   * @return
   */
  @GetMapping("/getCheckCode")
  public R getLoginCode(String phone) {
    boolean isExist = validatePhoneNumber(phone);
    if (!isExist){throw new ServiceException(ResultCode.Fail,"请输入正确的手机号");}
    return userService.getLoginCode(phone);
  }

  /**
   * 点赞
   * @param productId
   * @return
   */
  @PostMapping("/product/like/{productId}")
  public R like(@PathVariable(value = "productId") String productId){
    RBloomFilter<Object> productIdBloomFilter = redissonClient.getBloomFilter(ProductId_Bloom_Filter);
    if (!productIdBloomFilter.contains(productId)) {
      return R.fail(ResultCode.NotFindError, "商品不存在");
    }
    userService.like(productId);
    return R.ok();
  }

  /**
   * 取消点赞
   * @param productId
   * @return
   */
  @DeleteMapping("/product/unlike/{productId}")
  public R unlike(@PathVariable(value = "productId") String productId){
    RBloomFilter<Object> productIdBloomFilter = redissonClient.getBloomFilter(ProductId_Bloom_Filter);
    if (!productIdBloomFilter.contains(productId)) {
      return R.fail(ResultCode.NotFindError, "商品不存在");
    }
    return userService.unlike(productId);
  }
  @GetMapping("/share")
  public R share() {
    return userService.getQRCodeShare();
  }

  /**
   * 校验手机号
   * @param phoneNumber
   * @return
   */
  public static boolean validatePhoneNumber(String phoneNumber) {
    String regex = "^1[3-9]\\d{9}$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(phoneNumber);
    return matcher.matches();
  }
}
