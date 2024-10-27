package com.zyh.market.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.market.R;
import com.zyh.market.entity.PointsMallType;


import java.util.List;

public interface PointsMallTypeService extends IService<PointsMallType> {
    R<List<PointsMallType>> getTypeList();
}
