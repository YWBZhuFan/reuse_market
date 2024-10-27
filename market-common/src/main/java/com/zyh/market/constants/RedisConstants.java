package com.zyh.market.constants;

public class RedisConstants {
    public static final String USER_POINTS_PREFIX = "points:";
    public static final String USER_SHARE_CODE = "shareCode";
    public static final String CHECK_CODE_PREFIX = "check:code:";
    public static final String TOKEN_KEY = "token:login:token:";
    public static final String POINTS_MALL_TYPE = "pointsMallProductType";
    public static final String CLICK_COUNT = "clickCount";
    public static final String EXCHANGE_LIST = "exchangeList:";
    public static final String CHAT_MESSAGE = "chat:message:";
    public static final String FOLLOW_FOLLOW_USER = "follow:followUser:";
    public static final String LIKE_PRODUCT = "like:product:";
    public static final String PRODUCT_MALL_ALL = "productMall:All";
    public static final String PRODUCT_MALL_MAKEUP = "productMall:Makeup";
    public static final String PRODUCT_MALL_CARD = "productMall:Card";
    public static final String PRODUCT_MALL_NUMERAL = "productMall:Numeral";
    public static final String PRODUCT_MALL_FOOD = "productMall:Food";


    public static class RedissonClient{
        public static final String ProductId_Bloom_Filter = "productId_bloom_filter";
        public static final String USERID_BLOOM_FILTER = "userId_bloom_filter";
    }
}
