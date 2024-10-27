package com.zyh.market.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.market.R;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.dto.UpdateUserInfoDto;
import com.zyh.market.dto.UserAdminPageDto;
import com.zyh.market.dto.UserLoginDto;
import com.zyh.market.dto.UserRegisterDto;
import com.zyh.market.entity.*;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.mapper.FollowMapper;
import com.zyh.market.mapper.PointsDetailMapper;
import com.zyh.market.properties.ZhuFanProperties;
import com.zyh.market.service.FollowService;
import com.zyh.market.service.ProductLikeService;
import com.zyh.market.service.UserService;
import com.zyh.market.mapper.UserMapper;
import com.zyh.market.utils.AliGetCodeUtil;
import com.zyh.market.utils.QRCodeUtil;
import com.zyh.market.vo.FollowVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import static com.zyh.market.constants.Constants.QRCodeConstants.*;
import static com.zyh.market.constants.Constants.ShareCodeConstants.*;
import static com.zyh.market.constants.MySqlConstants.*;
import static com.zyh.market.constants.RedisConstants.*;
import static com.zyh.market.constants.RedisConstants.RedissonClient.USERID_BLOOM_FILTER;

/**
 * @author zhangyihua
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-01-28 16:52:33
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

  @Autowired private StringRedisTemplate redisTemplate;
  @Autowired private ZhuFanProperties zhuFanProperties;
  @Autowired private FollowMapper followMapper;
  @Autowired private FollowService followService;
  @Autowired private ProductLikeService productLikeService;
  @Autowired private FileStorageService fileStorageService;
  @Autowired private FileDetailServiceImpl fileDetailService;
  @Autowired private UserMapper userMapper;
  @Autowired private PointsDetailMapper pointsDetailMapper;
  @Autowired private RedissonClient redissonClient;
  @Autowired private RabbitTemplate rabbitTemplate;
  @Override
  public SaTokenInfo login(UserLoginDto request) {
    String userCode = request.getCode();
    String phone = request.getPhone();
    String redis_code = redisTemplate.opsForValue().get(CHECK_CODE_PREFIX + phone);
    if(StrUtil.isEmpty(redis_code) ) throw new ServiceException(ResultCode.Fail, "验证码错误");
    if ( !redis_code.equals(userCode)) throw new ServiceException(ResultCode.Fail, "验证码错误");
    //1.查询用户是否存在
    User user = lambdaQuery().eq(User::getPhone, request.getPhone()).one();
    //查询登录前是否已经存在token
    String oldToken = StpUtil.getTokenValueByLoginId(user.getId());
    String value = redisTemplate.opsForValue().get(TOKEN_KEY + oldToken);
    if (value != null){
      redisTemplate.delete(TOKEN_KEY + oldToken);
      StpUtil.kickout(user.getId());
    }
    StpUtil.login(user.getId());
    redisTemplate.delete(CHECK_CODE_PREFIX + request.getPhone());
    //返回token
    return StpUtil.getTokenInfo();
  }

  @Override
  public R userRegister(UserRegisterDto userRegisterDto) {
    String userCode = userRegisterDto.getCode();
    String phone = userRegisterDto.getPhone();
    String redis_code = redisTemplate.opsForValue().get(CHECK_CODE_PREFIX + phone);
    if(StrUtil.isEmpty(redis_code) ) return R.fail(ResultCode.Fail, "验证码错误");
    if ( !redis_code.equals(userCode)) return R.fail(ResultCode.Fail, "验证码错误");
    if (!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword()))
    {
      return R.fail(ResultCode.Fail, "两次密码不一致");
    }
    User user = lambdaQuery().eq(User::getPhone, userRegisterDto.getPhone()).one();
    if (!BeanUtil.isEmpty(user)){
      return R.fail(ResultCode.Fail, "您已经注册过啦");
    }
    String numbers = RandomUtil.randomNumbers(9);
    User userNew = User.builder().number(numbers)
            .phone(Long.valueOf(userRegisterDto.getPhone()))
            .password(userRegisterDto.getPassword())
            .avatar("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png")
            .nickName("用户" + numbers)
            .createTime(new Date().getTime())
            .status(9)
            .points(0).build();
    if (!StrUtil.isEmpty(userRegisterDto.getShareCode()) && userRegisterDto.getShareCode().length() == 6){
      String shareUserId = query().eq(SHARE_CODE, userRegisterDto.getShareCode()).one().getId();
      userNew.setInviterId(shareUserId);
    }
    int insert = userMapper.insert(userNew);
    if (insert<1){
      return R.fail(ResultCode.Fail);
    }
    redisTemplate.opsForValue().set(USER_POINTS_PREFIX+userNew.getId() , String.valueOf(userNew.getPoints()));
    redisTemplate.delete(CHECK_CODE_PREFIX + userNew.getPhone());
    RBloomFilter<Object> userIdBloomFilter = redissonClient.getBloomFilter(USERID_BLOOM_FILTER);
    userIdBloomFilter.add(userNew.getId());
    return R.ok();
  }

  /**
   * 获取新增关注列表
   * @return
   */
  @Override
  public R getNewFollow() {
    String userId = StpUtil.getLoginIdAsString();
    List<Follow> followList = followService.query().eq(FOLLOWER_ID, userId).list();
    if (followList.isEmpty()){
      return R.ok();
    }
    List<FollowVo> followVos = BeanUtil.copyToList(followList, FollowVo.class);
    List<FollowVo> followVoList = followVos.stream().map(followVo -> {
      User user = query().eq("id", followVo.getUserId()).one();
      followVo.setAvatar(user.getAvatar());
      followVo.setNickName(user.getNickName());
      return followVo;
    }).collect(Collectors.toList());
    int noReadCount = 0;
    for (FollowVo followVo : followVoList) {
      if (followVo.getIsRead() == 0){
        noReadCount++;
      }
    }
    for (FollowVo followVo : followVoList) {
      followVo.setNoReadCount(noReadCount);
    }
    followVoList.sort((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()));
    return R.ok(followVoList);
  }

  @Override
  public SaTokenInfo loginPwd(UserLoginDto request) {
    User user = lambdaQuery().eq(User::getPhone, request.getPhone()).one();
    if(BeanUtil.isEmpty(user))throw new ServiceException(ResultCode.Fail,"第一次登陆，请选择手机号登录");
    //查询登录前是否已经存在token
    String tokenKey = "token:login:token:";
    String oldToken = StpUtil.getTokenValueByLoginId(user.getId());
    String value = redisTemplate.opsForValue().get(tokenKey + oldToken);
    if (value != null){
      redisTemplate.delete(tokenKey + oldToken);
      StpUtil.kickout(user.getId());
    }
    String md5Pwd = SaSecureUtil.md5(request.getPassword());
    String relPwd = SaSecureUtil.md5(user.getPassword());
    if(!md5Pwd.equals(relPwd)) throw new ServiceException(ResultCode.Fail, "手机号或密码错误");
    user.setProvince(request.getProvince());
    user.setCity(request.getCity());
    user.setUpdateTime(new Date().getTime());
    boolean update = updateById(user);
    if (!update) throw new ServiceException(ResultCode.Fail);
    StpUtil.login(user.getId());
    return StpUtil.getTokenInfo();
  }
  
  @Override
  public void updateUserInfo(UpdateUserInfoDto dto) {
    String id = StpUtil.getLoginIdAsString();
    User user = getById(id);
    if (BeanUtil.isEmpty(user)) throw new ServiceException(ResultCode.Fail);
    user.setCheckNickName(dto.getNickName());
    user.setCheckIntro(dto.getIntro());
    user.setCheckAvatar(dto.getAvatar());
    user.setCheckStatus(0);
    boolean update = updateById(user);
    if (!update) throw new ServiceException(ResultCode.UpdateError);
  }
  
  @Override
  public Map getNum1UserStat() {
  return null;
  }
  
  @Override
  public List<Map> getUserStat() {
    LocalDate today = LocalDate.now();
    LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    
    List<Map> weeklyUserData = new ArrayList<>();
    String[] weekDays = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    
    for (int i = 0; i < 7; i++) {
      LocalDate currentDate = monday.plusDays(i);
      
      long newUsers = __getNewUsersCount(currentDate);
      long activeUsers = __getActiveUsersCount(currentDate);
      
      Map<String, Object> userData = new HashMap<>();
      userData.put("date", weekDays[i]);
      userData.put("new", newUsers);
      userData.put("active", activeUsers);
      
      weeklyUserData.add(userData);
    }
    
    return weeklyUserData;
  }

  private long __getActiveUsersCount(LocalDate currentDate) {
    //根据currentDate 获取当日0点到24点的时间戳
    long start = currentDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000;
    long end = currentDate.atStartOfDay().plusDays(1).toEpochSecond(ZoneOffset.UTC) * 1000;
    Long count = lambdaQuery().between(User::getUpdateTime, start, end).count();
    return count;
  }
  
  private long __getNewUsersCount(LocalDate currentDate) {
    //根据currentDate 获取当日0点到24点的时间戳
    long start = currentDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000;
    long end = currentDate.atStartOfDay().plusDays(1).toEpochSecond(ZoneOffset.UTC) * 1000;
    Long count = lambdaQuery().between(User::getCreateTime, start, end).count();
    return count;
  }
  
  
  @Override
  public R<User> getUserInfo() {
    Object loginId = StpUtil.getLoginId();
    User user = lambdaQuery().eq(User::getId, loginId).one();
    if (user == null) {
      throw new ServiceException(ResultCode.NotFindError, "用户不存在");
    }
    return R.ok(user);
  }
  
  @Override
  public R getLoginCode(String phone) {
    if (phone.isEmpty()){
      throw new ServiceException(ResultCode.Fail, "手机号不能为空");
    }
    if (BooleanUtil.isTrue(redisTemplate.hasKey(CHECK_CODE_PREFIX + phone))){
      return R.fail(ResultCode.Fail, "请勿频繁获取验证码");
    }
    rabbitTemplate.convertAndSend("amq.direct","LoginCode.success", "LoginCode-"+phone);
    return R.ok();
  }
  
  @Override
  public R<User> getUserInfo(String userId) {
    User user = lambdaQuery().eq(User::getId, userId).one();
    if (user == null) {
      throw new ServiceException(ResultCode.NotFindError, "用户不存在");
    }
    return R.ok(user);
  }
  
  @Override
  public void updateUserInfoDetail(UpdateUserInfoDto dto) {
    String id = StpUtil.getLoginIdAsString();
    User user = getById(id);
    if(BeanUtil.isEmpty(user))  throw new ServiceException(ResultCode.NotFindError);
    if(StrUtil.isNotEmpty(dto.getPassword())){
      if (!dto.getPassword().equals(dto.getPasswordCheck())) throw new ServiceException(ResultCode.ValidateError,"输入密码不一致");
      String md5 = SaSecureUtil.md5(dto.getPassword());
      user.setPassword(md5);
    }
    boolean update = updateById(user);
    if(!update) throw new ServiceException(ResultCode.UpdateError);
  }
  
  @Override
  public Page getUserList(UserAdminPageDto dto) {
    Page<User> page = lambdaQuery()
      .like(StrUtil.isNotEmpty(dto.getKey()), User::getNickName, dto.getKey()).or()
      .like(StrUtil.isNotEmpty(dto.getKey()), User::getNumber, dto.getKey())
      .eq(null != dto.getCheckStatus(), User::getCheckStatus, dto.getCheckStatus())
      .orderByDesc(User::getCreateTime)
      .page(new Page<>(dto.getPageNumber(), dto.getPageSize()));
    return page;
  }

  @Override
  public R followUser(String followerId) {
    String userId = StpUtil.getLoginIdAsString();
    if (userId.equals(followerId)){
      return R.fail(ResultCode.Fail,"不能关注自己");
    }
    Boolean isExist = redisTemplate.opsForSet().isMember("follow:followUser:" + userId, followerId);
    Follow follow = followMapper.selectOne(new LambdaQueryWrapper<Follow>().eq(Follow::getUserId, userId).eq(Follow::getFollowerId, followerId));
    if (BooleanUtil.isTrue(isExist) || !BeanUtil.isEmpty(follow)){
      throw new ServiceException(ResultCode.Fail,"已关注此用户");
    }
    redisTemplate.opsForSet().add("follow:followUser:"+userId,followerId);
    return R.ok("关注成功");
  }

  @Override
  public R delFollowUser(String followerId) {
    String userId = StpUtil.getLoginIdAsString();
    if (userId.equals(followerId)){
      return R.fail(ResultCode.Fail,"不能关注自己");
    }
    Boolean isExist = redisTemplate.opsForSet().isMember(FOLLOW_FOLLOW_USER + userId, followerId);
    if (BooleanUtil.isTrue(isExist)){
      redisTemplate.opsForSet().remove(FOLLOW_FOLLOW_USER + userId,followerId);
      return R.ok("取关成功");
    }
    Follow follow = followMapper.selectOne(new LambdaQueryWrapper<Follow>().eq(Follow::getUserId, userId).eq(Follow::getFollowerId, followerId));
    if (BeanUtil.isEmpty(follow) && BooleanUtil.isFalse(isExist)){
      return R.fail(ResultCode.NotFindError,"未关注此用户");
    }
    if (BooleanUtil.isFalse(isExist) && !BeanUtil.isEmpty(follow)){
      followMapper.delete(new LambdaQueryWrapper<Follow>().eq(Follow::getUserId, userId).eq(Follow::getFollowerId, followerId));
      return R.ok("取关成功");
    }
    /*int result = followMapper.delete(new LambdaQueryWrapper<Follow>().eq(Follow::getUserId, userId).eq(Follow::getFollowerId, followerId));
    if (result < 1){
      throw  new ServiceException(ResultCode.Fail);
    }
    redisTemplate.opsForSet().remove("follow:followUser:"+userId,followerId);*/
    return R.ok("取关成功");
  }

  /**
   * 点赞
   * @param productId
   */
  @Override
  public R like(String productId) {
    String userId = StpUtil.getLoginIdAsString();
    Boolean isExist = redisTemplate.opsForSet().isMember(LIKE_PRODUCT + userId, productId);
    if (BooleanUtil.isTrue(isExist)){
      return R.fail(ResultCode.Fail,"不能重复点赞哦");
    }
    redisTemplate.opsForSet().add(LIKE_PRODUCT+userId,productId);
    return R.ok("点赞成功");
  }

  @Override
  public R unlike(String productId) {
    String userId = StpUtil.getLoginIdAsString();
    Boolean isExist = redisTemplate.opsForSet().isMember(LIKE_PRODUCT + userId, productId);
    if (BooleanUtil.isTrue(isExist)){
      redisTemplate.opsForSet().remove(LIKE_PRODUCT + userId,productId);
    }else {
      productLikeService.remove(new LambdaQueryWrapper<ProductLike>().eq(ProductLike::getUserId, userId).eq(ProductLike::getProductId, productId));
    }
    return R.ok();
  }

  /**
   * 签到
   * @return
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public R signIn() {
    String userId = StpUtil.getLoginIdAsString();
    LocalDate now = LocalDate.now();
    String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
    String signKey = "sign" + userId + keySuffix;
    //获取今天是本月的第几天
    int dayOfMonth = now.getDayOfMonth();
    Boolean isSign = redisTemplate.opsForValue().getBit(signKey, dayOfMonth - 1);
    if (Boolean.TRUE.equals(isSign)){
      return R.fail(ResultCode.Fail,"今天已经签到过了");
    }
    try {
      redisTemplate.opsForValue().setBit(signKey, dayOfMonth - 1, true);
      //用户积分+5
      Integer points = query().eq(ID, userId).one().getPoints();
      redisTemplate.opsForValue().increment(USER_POINTS_PREFIX + userId, 2);
      update(new LambdaUpdateWrapper<User>().eq(User::getId, userId).setSql("points = points + 2"));
      pointsDetailMapper.insert(new PointsDetail().setUserId(userId).setPointsChange(2).setPointsTotal(points+2).setType(1).setCreateTime(LocalDateTime.now()));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return R.ok("签到成功");
  }

  /**
   * 获取分享二维码
   * @return
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public R getQRCodeShare() {
    String userId = StpUtil.getLoginIdAsString();
    String fileName = FILE_PREFIX+userId+PNG_SUFFIX;
    FileDetail fileDetail = fileDetailService.query().eq(OBJECT_ID, userId).eq(OBJECT_TYPE, "分享二维码").one();
    if (BeanUtil.isEmpty(fileDetail)){
      try {
        String shareCode;
        do{
          shareCode = createShareCode();
        }while (BooleanUtil.isTrue(redisTemplate.opsForSet().isMember(USER_SHARE_CODE, shareCode)));
        redisTemplate.opsForSet().add(USER_SHARE_CODE, shareCode);
        String shareUrl = zhuFanProperties.getShareUrl()+ shareCode;
        File QRCode = QRCodeUtil.LogoQrCode(shareUrl, zhuFanProperties.getLogoPath(), fileName);
        FileInfo fileInfo = null;
        try {
          fileInfo = fileStorageService.of(QRCode)
                  .setPath(QRCode_FILE_PATH_Prefix)
                  .setName(fileName)
                  .setObjectId(userId)
                  .setObjectType("分享二维码").upload();
          update().eq(ID,userId).set(SHARE_CODE,shareCode).update();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
        String url1 = fileInfo.getUrl();
        return R.ok(url1);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    String url = fileDetail.getUrl();
    return R.ok(url);
  }

  @Override
  public R isSignIn() {
    String userId = StpUtil.getLoginIdAsString();
    LocalDate now = LocalDate.now();
    String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
    String signKey = "sign" + userId + keySuffix;
    //获取今天是本月的第几天
    int dayOfMonth = now.getDayOfMonth();
    Boolean isSign = redisTemplate.opsForValue().getBit(signKey, dayOfMonth - 1);
    if (BooleanUtil.isTrue(isSign)){
      return R.ok("已签到");
    }
    return R.ok("未签到");
  }

  public String createShareCode(){
    // 创建SecureRandom对象
    SecureRandom secureRandom = new SecureRandom();
    // 生成邀请码
    StringBuilder sb = new StringBuilder(CODE_LENGTH);
    // 生成2位大写字母
    for (int i = 0; i < 2; i++) {
      int index = secureRandom.nextInt(UPPER_CASE_LETTERS.length());
      sb.append(UPPER_CASE_LETTERS.charAt(index));
    }
    // 生成2位小写字母
    for (int i = 0; i < 2; i++) {
      int index = secureRandom.nextInt(LOWER_CASE_LETTERS.length());
      sb.append(LOWER_CASE_LETTERS.charAt(index));
    }
    // 生成2位数字
    for (int i = 0; i < 2; i++) {
      int index = secureRandom.nextInt(NUMBERS.length());
      sb.append(NUMBERS.charAt(index));
    }
    // 打乱顺序
    List<Character> charList = new ArrayList<>(sb.length());
    for (char c : sb.toString().toCharArray()) {
      charList.add(c);
    }
    Collections.shuffle(charList, secureRandom);
    // 重新构建邀请码
    sb.setLength(0); // 清空StringBuilder
    for (char c : charList) {
      sb.append(c);
    }
      return sb.toString();
  }

}




