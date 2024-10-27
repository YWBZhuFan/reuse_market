package com.zyh.market.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.zyh.market.R;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.constants.SystemConstants;
import com.zyh.market.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;

import static com.zyh.market.constants.MySqlConstants.URL;

/**
 * @author zhangyihua
 * @version 1.0
 * @description TODO
 * @date 2024/3/1 21:48
 */
@RestController
@RequestMapping("/upload")
@Slf4j
@SaCheckLogin
public class UploadController {

  @Autowired private FileStorageService fileStorageService;
  
  //minio
  @PostMapping("/image")
  public R uploadImage(@RequestParam("file") MultipartFile image,
                       @RequestParam("fileType") String fileType) {
    try {
      String fileName = UUID.randomUUID() + "." + StrUtil.subAfter(image.getOriginalFilename(), ".", true);
      String objectName = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "/";
      FileInfo fileInfo = fileStorageService.of(image)
              .setPath(objectName)
              .setName(fileName)
              .setObjectType(fileType)
              .setObjectId(StpUtil.getLoginIdAsString())
              .upload();
      HashMap<String, String> result = new HashMap<>();
      result.put("name", fileName);
      result.put(URL, fileInfo.getUrl());
      return R.ok(result);
    } catch (Exception e) {
      log.error(e.toString());
      throw new ServiceException(ResultCode.Fail);
    }
  }

  @GetMapping("/image/delete")
  public R deleteBlogImg(@RequestParam("fileName") String fileName) {
    File file = new File(SystemConstants.IMAGE_UPLOAD_DIR, fileName);
    if (file.isDirectory()) {
      return R.fail(ResultCode.Fail);
    }
    boolean del = FileUtil.del(file);
    if (del) {
      log.info("文件删除成功，{}", fileName);
    }
    return R.ok();
  }
  
  private String createNewFileName(String originalFilename) {
    // 获取后缀
    String suffix = StrUtil.subAfter(originalFilename, ".", true);
    // 生成目录
    String name = UUID.randomUUID().toString();
    int hash = name.hashCode();
    int d1 = hash & 0xF;
    int d2 = (hash >> 4) & 0xF;
    // 判断目录是否存在
    File dir = new File(SystemConstants.IMAGE_UPLOAD_DIR, StrUtil.format("/{}/{}", d1, d2));
    if (!dir.exists()) {
      dir.mkdirs();
    }
    // 生成文件名
    return StrUtil.format("/{}/{}/{}.{}", d1, d2, name, suffix);
  }
}
