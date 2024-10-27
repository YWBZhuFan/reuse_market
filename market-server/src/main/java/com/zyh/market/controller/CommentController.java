package com.zyh.market.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zyh.market.R;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.dto.CreateCommentDto;
import com.zyh.market.entity.Comment;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.service.CommentService;
import com.zyh.market.vo.CommentVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zyh.market.constants.MySqlConstants.ID;


@RestController
@RequestMapping("/comment")
@SaCheckLogin
public class CommentController {
  @Autowired private CommentService commentService;
  
  @PostMapping
  public R saveComment(@RequestBody CreateCommentDto dto) {
    commentService.saveComment(dto.getProductId(), dto.getParentId(), dto.getContent());
    return R.ok();
  }

  @GetMapping
  public R getComment(){
    return commentService.getComment();
  }

  @PutMapping
  public R updateComment(@RequestBody List<String> ids) {
    UpdateWrapper<Comment> updateWrapper = new UpdateWrapper<>();
    updateWrapper.in(ID, ids);
    Comment comment = new Comment();
    comment.setIsRead(1);
    commentService.update(comment, updateWrapper);
    return R.ok();
  }
  
  @GetMapping("/byProduct")
  public R<Map> getCommentList(@RequestParam("productId") String productId) {
    HashMap map = commentService.getCommentList(productId);
    return R.ok(map);
  }
  
  @DeleteMapping("/{id}")
  public R delComment(@PathVariable("id") String id) {
    boolean update = commentService.removeById(id);
    if (!update) throw new ServiceException(ResultCode.DeleteError);
    return R.ok();
  }
}
