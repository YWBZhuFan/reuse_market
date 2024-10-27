package com.zyh.market.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.j2objc.annotations.AutoreleasePool;
import com.zyh.market.R;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.dto.UpdateUserInfoDto;
import com.zyh.market.entity.Comment;
import com.zyh.market.entity.Follow;
import com.zyh.market.entity.User;
import com.zyh.market.service.FollowService;
import com.zyh.market.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.zyh.market.constants.MySqlConstants.USER_ID;
import static com.zyh.market.constants.RedisConstants.FOLLOW_FOLLOW_USER;
import static com.zyh.market.constants.RedisConstants.RedissonClient.USERID_BLOOM_FILTER;

/**
 * @author zhangyihua
 * @version 1.0
 * @description TODO
 * @date 2024/1/28 15:01
 */
@RestController
@RequestMapping("/user")
@SaCheckLogin
@Slf4j
public class UserController {
  @Autowired private UserService userService;
  @Autowired private RedissonClient redissonClient;
  @Autowired private FollowService followService;
  @Autowired private StringRedisTemplate redisTemplate;
  
  @GetMapping("/logout")
  public R logout(){
    StpUtil.logout(StpUtil.getLoginId());
    return R.ok();
  }
  @GetMapping("/getUserInfo")
  public R<User> getUserInfo() {
    return userService.getUserInfo();
  }
  
  @GetMapping("/getUserInfo/byId")
  public R<User> getUserInfo(String userId) {
    return userService.getUserInfo(userId);
  }

  @PutMapping
  public R updateUserInfo(@RequestBody UpdateUserInfoDto dto){
    userService.updateUserInfo(dto);
    return R.ok();
  }
  
  @PutMapping("/password")
  public R updateUserInfoPass(@RequestBody UpdateUserInfoDto dto) {
    userService.updateUserInfoDetail(dto);
    return R.ok();
  }

  /**
   * 用户签到
   * @return
   */
  @PostMapping("/signIn")
  public R signIn() {
    return userService.signIn();
  }

  /**
   * 获取用户是否签到
   */
  @GetMapping("/isSignIn")
  public R isSignIn() {
    return userService.isSignIn();
  }

  /**
   * 关注用户
   * @param followUserId
   * @return
   */
  @PostMapping("/followUser/{id}")
  public R followUser(@PathVariable(value = "id") String followUserId) {
    boolean isExist = isExist(followUserId);
    log.info("被关注用户id为{}", followUserId);
    if (!isExist) {
      return R.fail(ResultCode.NotFindError, "该用户不存在或已注销");
    }
    return userService.followUser(followUserId);
  }

  /**
   * 取消关注
   * @param followUserId
   * @return
   */
  @DeleteMapping("/followUser/{id}")
  public R delFollowUser(@PathVariable(value = "id") String followUserId) {
    boolean isExist = isExist(followUserId);
    if (!isExist) {return R.fail(ResultCode.NotFindError, "该用户不存在或已注销");}
    return userService.delFollowUser(followUserId);
  }

  @GetMapping("/followUser/list")
  public R<List<String>> getFollowUserList() {
    String userId = StpUtil.getLoginIdAsString();
    Set<String> followUserListRedis = redisTemplate.opsForSet().members(FOLLOW_FOLLOW_USER + userId);
    //查询关注用户列表
    List<String> followUserList = followService.query().eq(USER_ID, userId).list()
            .stream().map((Follow::getFollowerId)).collect(Collectors.toList());
    if (followUserListRedis != null) {
      followUserList.addAll(followUserListRedis);
    }
    return R.ok(followUserList);
  }

  /**
   * 获取新关注用户
   * @return
   */
  @GetMapping("/followUser/newFollow")
  public R getNewFollow() {
    return userService.getNewFollow();
  }

  @PutMapping("/followUser/isRead")
  public R isRead(@RequestBody List<String> ids) {
    if (ids.isEmpty()){
      return R.ok();
    }
    followService.update().in("id", ids).set("is_read", 1).update();
    return R.ok();
  }

  /**
   * 判断用户id是否存在
   * @param followUserId
   * @return
   */
  private boolean isExist(String followUserId) {
    RBloomFilter<Object> userIdBloomFilter = redissonClient.getBloomFilter(USERID_BLOOM_FILTER);
      return userIdBloomFilter.contains(followUserId);
  }
}
