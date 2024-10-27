package market;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.excel.util.ListUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zyh.market.ReuseMarketApplication;
import com.zyh.market.entity.ExcelModel;
import com.zyh.market.entity.Follow;
import com.zyh.market.entity.ProductInfo;
import com.zyh.market.entity.User;
import com.zyh.market.service.ProductInfoService;
import com.zyh.market.service.UserService;
import com.zyh.market.utils.QRCodeUtil;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.zyh.market.constants.Constants.QRCodeConstants.*;
import static com.zyh.market.constants.Constants.ShareCodeConstants.CHARACTERS;
import static com.zyh.market.constants.Constants.ShareCodeConstants.CODE_LENGTH;


@SpringBootTest(classes = ReuseMarketApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class Demo {
    @Autowired RedissonClient redissonClient;
    @Autowired private UserService userService;
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    @Autowired private StringRedisTemplate redisTemplate;
    @Autowired private ProductInfoService productInfoService;
    @Test
    public void createOrderNum() {
        long time = System.currentTimeMillis();
        // 将毫秒时间戳转换为Instant对象
        Instant instant = Instant.ofEpochMilli(time);
        // 将Instant对象转换为LocalDateTime对象，使用系统默认时区
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        // 创建一个DateTimeFormatter对象，指定输出格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        // 使用DateTimeFormatter对象格式化LocalDateTime
        String formattedDateTime = localDateTime.format(formatter);
        //生成5位随机数作为订单号前缀
        String timePrefix = RandomUtil.randomNumbers(5);
        String orderNum = timePrefix + formattedDateTime;
        System.out.println(orderNum);
    }

    @Test
    public void test2() {
        // 获取当前时间的毫秒级时间戳，并格式化为"yyyyMMddHHmmssSSS"
        String timestamp = LocalDateTime.now().format(formatter);

        // 生成一个5位的随机数，这里使用ThreadLocalRandom保证线程安全
        int randomNum = ThreadLocalRandom.current().nextInt(10000, 100000);

        // 拼接时间戳和随机数生成订单号
        System.out.println("ORDER_" + timestamp + "_" + randomNum);
    }

    @Test
    public void initUserIdBloomFilter() {
        List<User> list = userService.list();
        List<String> userIdList = list.stream().map(User::getId).collect(Collectors.toList());
        RBloomFilter<Object> userIdBloomFilter = redissonClient.getBloomFilter("userId_bloom_filter");
        userIdBloomFilter.tryInit(10000, 0.01);
        userIdList.forEach(userIdBloomFilter::add);
        System.out.println(userIdBloomFilter.contains("2")?"存在":"不存在");
    }
    @Test
    public void initProductIdBloomFilterData() {
        List<ProductInfo> list = productInfoService.list();
        List<String> productIdList = list.stream().map(ProductInfo::getId).collect(Collectors.toList());
        /*List<String> productIdList = new ArrayList<>();
        productIdList.add("1804450692031696897");
        productIdList.add("1804509881571524610");
        productIdList.add("1810890477026291713");
        productIdList.add("1812083066094198786");
        productIdList.add("1822554770167128066");
        productIdList.add("1822559441229295617");
        productIdList.add("1825805349313523713");*/
        RBloomFilter<Object> productIdBloomFilter = redissonClient.getBloomFilter("productId_bloom_filter");
        productIdBloomFilter.tryInit(10000, 0.01);
        productIdList.forEach(productIdBloomFilter::add);
        System.out.println(productIdBloomFilter.contains("1822559441229295617")?"存在":"不存在");
    }

    @Test
    public void test6() throws IOException {
        String fileName = FILE_PREFIX+"2";
        File QRCode = QRCodeUtil.LogoQrCode(SHARE_URL, LOGO_PATH, fileName);
        // 指定目标路径
        String targetPath = "E:/桌面/" + fileName+PNG_SUFFIX;
        // 复制文件
        FileUtils.copyFile(QRCode, new File(targetPath));
    }


    @Test
    public void test7() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = secureRandom.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        System.out.println(sb);
    }

    @Test
    public void createExcel() {
        List<ExcelModel> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            ExcelModel data = new ExcelModel();
            data.setDatetime("字符串" + i);
            data.setDigital("");
            data.setCoupon("0");
            data.setBook("0.56");
            data.setLife("0.56");
            data.setDaySale("0.56");
            list.add(data);
        }

    }

    @Test
    public void test5() {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 获取当前月份第一天
        LocalDate firstDayOfCurrentMonth = currentDate.withDayOfMonth(1);
        // 获取当前月份最后一天
        LocalDate lastDayOfCurrentMonth = firstDayOfCurrentMonth.plusMonths(1).minusDays(1);

        // 创建日期格式器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        // 生成日期列表
        List<String> dates = new ArrayList<>();
        while (!firstDayOfCurrentMonth.isAfter(lastDayOfCurrentMonth)) {
            dates.add(firstDayOfCurrentMonth.format(formatter));
            firstDayOfCurrentMonth = firstDayOfCurrentMonth.plusDays(1);
        }
        // 倒序排列
        Collections.reverse(dates);
        System.out.println(dates);
    }

    @Test
    public void test() {
        String s = "access_token=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&scope=&token_type=bearer";
        System.out.println(s.split("&")[0].split("=")[1]);
    }

    @Test
    public void test8() {
        final String UPPER_CASE_LETTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ";
        final String LOWER_CASE_LETTERS = "abcdefghjkmnpqrstuvwxyz";
        final String NUMBERS = "123456789";
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
        String shareCode = sb.toString();
    }

    @Test
    public void optionalQuickly(){
        Follow follow = new Follow();
        follow.setId("1");
        Optional.ofNullable(follow).map(Follow::getId).ifPresent(System.out::println);
    }
}
