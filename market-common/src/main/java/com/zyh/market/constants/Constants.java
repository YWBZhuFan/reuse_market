package com.zyh.market.constants;

public class Constants {
    public static final class QRCodeConstants {
        public static final String LOGO_PATH = "E:\\LearningTools\\LOGO.png";
        public static final String SHARE_URL = "http://www.ycowokb.top:8080/#/login/register?code=";
        public static final String QRCode_Share_URL_Prefix = "http://sii2btw3l.hd-bkt.clouddn.com/reuse-market-images/QRCodeShare/";
        public static final String QRCode_FILE_PATH_Prefix = "QRCodeShare/";
        public static final String FILE_PREFIX = "share-";
        public static final String PNG_SUFFIX = ".png";
        public static final String JPG_SUFFIX = ".jpg";
    }

    public static final class ShareCodeConstants {
        public static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz0123456789";
        public static final String UPPER_CASE_LETTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ";
        public static final String LOWER_CASE_LETTERS = "abcdefghjkmnpqrstuvwxyz";
        public static final String NUMBERS = "123456789";
        public static final int CODE_LENGTH = 6;
    }

    public static final class AliPayConstants {
        public static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
        public static final String RETURN_URL = "http://localhost:8080/#/paymentPay?paymentOrderId="; //支付宝回调地址
        public static final String FORMAT = "JSON";
        public static final String CHARSET = "UTF-8";
        public static final String SIGN_TYPE = "RSA2";
    }

    public static final class WebOriginsConstants {
        public static final String LOCALHOST8080 = "http://localhost:8080";
        public static final String LOCALHOST9528 = "http://localhost:9528";
        public static final String ONLINE8080 = "http://www.ycowokb.top:8080";
        public static final String ONLINE = "http://www.ycowokb.top";
        public static final String ONLINE9528 = "http://www.ycowokb.top:9528";
    }

    public static final class HtmlCallbackUrlConstants {
        public static final String CALLBACK_ERROR_URL = "http://www.ycowokb.top/#/login";
        public static final String CALLBACK_SUCCESS_URL = "http://www.ycowokb.top/#/login/callback";
        public static final String BINDING_SUCCESS_URL = "http://www.ycowokb.top/#/bindingCallback";
    }

    public static final class OAuthLoginTypeConstants {
        public static final String GITEE = "GiteeAuthServiceImpl";
        public static final String GITHUB = "GithubAuthServiceImpl";
        public static final String BINDING = "binding";
        public static final String LOGIN = "login";
        public static final String NO_BINDING = "请先绑定闲宝平台账号";
        public static final String ALREADY_BINDING = "您已绑定该账号";
        public static final String OTHER_BINDING_GITEE = "Gitee账号已被其他账号绑定";
        public static final String BINDING_SUCCESS = "绑定成功";
        public static final String BINDING_ERROR = "绑定失败";
        public static final String ERROR = "未知错误";
    }

    public static final class OtherConstants {
        public static final String RESUBMIT_KEY = "resubmit:";
    }
}
