package com.zyh.market.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("file_detail")
public class FileDetail {
    private String id;
    private String url;
    private Integer size;
    private String filename;
    private String originalFilename;
    private String basePath;
    private String path;
    private String ext;
    private String platform;
    private String objectId;
    private String objectType;
    private LocalDate createTime;
}
