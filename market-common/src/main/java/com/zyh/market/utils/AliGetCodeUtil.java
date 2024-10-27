package com.zyh.market.utils;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.teaopenapi.models.Config;


public class AliGetCodeUtil {

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }

    public static void SendCode(String code, String phone) throws Exception {
        Config config = new Config()
                //这里修改为我们上面生成自己的AccessKey ID
                .setAccessKeyId("LTAI5tPgKmi1VBiYXzt8p3X1")
                //这里修改为我们上面生成自己的AccessKey Secret
                .setAccessKeySecret("U7unq7X3hQBl34jOwyRqCyfQjMgyf6");
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        Client client = new Client(config);
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName("闲宝二手交易平台")//短信签名
                .setTemplateCode("SMS_471760003")//短信模板
                .setPhoneNumbers(phone)//这里填写接受短信的手机号码
                .setTemplateParam("{\"code\":\""+code+"\"}");//验证码
        // 复制代码运行请自行打印 API 的返回值
        client.sendSms(sendSmsRequest);
    }
}
