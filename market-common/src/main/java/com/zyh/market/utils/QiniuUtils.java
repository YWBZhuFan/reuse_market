package com.zyh.market.utils;


import com.alibaba.fastjson.JSON;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import com.zyh.market.constants.ResultCode;
import com.zyh.market.exception.ServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;



/**
 * @author zhangyihua
 * @version 1.0
 * @description TODO
 * @date 2023/2/22 16:08
 */
@Component
public class QiniuUtils {
    //预览外链
    public static final String url = "http://rqh2ajoy4.hn-bkt.clouddn.com/";
    private String accessKey = "KmALvfpot_eXEGuUcyRoTW0JltOvGY6taZIaXWBw";
    private String SecretKey = "ZPGpK_wUY59u7kbJx-6TlD-lq0iZnY-Xinwz0x0v";
    private String bucket = "zhangyihua";

    public boolean upload(MultipartFile file, String fileName) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
        UploadManager uploadManager = new UploadManager(cfg);
        //上传文件
        try {
            Auth auth = Auth.create(accessKey, SecretKey);
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(file.getBytes(), fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            return true;
        } catch (Exception ex) {
            throw new ServiceException(ResultCode.Fail);
        }
    }
}
