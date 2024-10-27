package com.zyh.market.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.market.R;
import com.zyh.market.dto.SystemCommentPageDto;
import com.zyh.market.entity.Comment;


import java.util.HashMap;

public interface CommentService extends IService<Comment> {
  HashMap getCommentList(String productId);
  
  void saveComment(String productId, String parentId, String content);
  
  Page getCommentPage(SystemCommentPageDto dto);

  R getComment();
}
