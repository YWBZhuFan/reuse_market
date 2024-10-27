package com.zyh.market.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.market.entity.FileDetail;
import com.zyh.market.mapper.FileDetailMapper;
import lombok.SneakyThrows;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.recorder.FileRecorder;
import org.dromara.x.file.storage.core.upload.FilePartInfo;
import org.springframework.stereotype.Service;

import static com.zyh.market.constants.MySqlConstants.ID;
import static com.zyh.market.constants.MySqlConstants.URL;

@Service
public class FileDetailServiceImpl extends ServiceImpl<FileDetailMapper, FileDetail> implements FileRecorder {

    @Override
    public boolean save(FileInfo fileInfo) {
        FileDetail fileDetail = BeanUtil.copyProperties(fileInfo, FileDetail.class);
        boolean isSuccess = save(fileDetail);
        if (isSuccess){
            fileInfo.setId(fileDetail.getId());
        }
        return isSuccess;
    }

    @Override
    @SneakyThrows
    public void update(FileInfo fileInfo) {
        FileDetail detail = BeanUtil.copyProperties(fileInfo, FileDetail.class);
        QueryWrapper<FileDetail> queryWrapper = new QueryWrapper<FileDetail>()
                .eq(detail.getUrl() != null, URL, detail.getUrl())
                .eq(detail.getId() != null, ID, detail.getId());
        update(detail, queryWrapper);
    }

    /**
     * 根据 url 获取文件信息
     * @param url
     * @return
     */
    @Override
    public FileInfo getByUrl(String url) {
        FileDetail fileDetail = query().eq(URL, url).one();
        if (BeanUtil.isEmpty(fileDetail)){
            return null;
        }
        return BeanUtil.copyProperties(fileDetail, FileInfo.class);
    }

    /**
     * 根据 url 删除文件信息
     */
    @Override
    public boolean delete(String url) {
        remove(new QueryWrapper<FileDetail>().eq(URL, url));
        return true;
    }

    @Override
    public void saveFilePart(FilePartInfo filePartInfo) {
    }

    @Override
    public void deleteFilePartByUploadId(String s) {
    }
}
