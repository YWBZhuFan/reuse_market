package com.zyh.market.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.market.R;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.dto.SystemCommentPageDto;
import com.zyh.market.entity.Comment;
import com.zyh.market.entity.ProductInfo;
import com.zyh.market.entity.User;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.mapper.CommentMapper;
import com.zyh.market.service.CommentService;
import com.zyh.market.service.ProductInfoService;
import com.zyh.market.service.UserService;
import com.zyh.market.vo.CommentVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.zyh.market.constants.MySqlConstants.*;


@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

  @Autowired private UserService userService;
  @Autowired private ProductInfoService productInfoService;
  
  @Override
  public HashMap getCommentList(String productId) {
    ArrayList<CommentVo> resultList = new ArrayList<>();
    List<Comment> commentList = lambdaQuery().eq(Comment::getProductId, productId).orderByAsc(Comment::getCreateTime).list();
    List<CommentVo> commentVoList = commentList.stream().map(item -> {
      CommentVo bean = BeanUtil.toBean(item, CommentVo.class);
      if (!BeanUtil.isEmpty(bean)) {
        User user = userService.getById(item.getPubUserId());
        bean.setPubAvatar(user.getAvatar());
        bean.setPubProvince(user.getProvince());
      }
      return bean;
    }).collect(Collectors.toList());
    for (CommentVo commentVo : commentVoList) {
      if (StrUtil.isNotEmpty(commentVo.getParentId())) {
        getParentComment(commentVo, commentVoList);
      } else {
        resultList.add(commentVo);
      }
    }
    HashMap<Object, Object> map = new HashMap<>();
    map.put("commentList", resultList);
    map.put("commentCount", commentList.size());
    return map;
  }

  private void getParentComment(CommentVo commentVo, List<CommentVo> commentVoList) {
    for (CommentVo vo : commentVoList) {
      if (commentVo.getParentId().equals(vo.getId())) {
        if (vo.getCommentChildren() == null) {
          ArrayList<CommentVo> list = new ArrayList<>();
          list.add(commentVo);
          vo.setCommentChildren(list);
        } else {
          vo.getCommentChildren().add(commentVo);
        }
      }
    }
  }
  
  @Override
  public void saveComment(String productId, String parentId, String content) {
    Comment comment = new Comment();
    comment.setProductId(productId);
    comment.setContent(content);
    comment.setCreateTime(new Date().getTime());
    String loginId = StpUtil.getLoginIdAsString();
    comment.setPubUserId(loginId);
    User user = userService.getById(loginId);
    comment.setPubNickname(user.getNickName());
    if (StrUtil.isNotEmpty(parentId)) {
      Comment parentComment = getById(parentId);
      comment.setParentUserId(parentComment.getPubUserId());
      comment.setParentUserNickname(parentComment.getPubNickname());
      if (StrUtil.isNotEmpty(parentComment.getParentId())) {
        comment.setParentId(parentComment.getParentId());
      } else {
        comment.setParentId(parentId);
      }
    }
    boolean save = save(comment);
    if (!save) throw new ServiceException(ResultCode.SaveError);
  }
  
  @Override
  public Page getCommentPage(SystemCommentPageDto dto) {
    Page<Comment> page = lambdaQuery()
      .like(StrUtil.isNotEmpty(dto.getKey()), Comment::getPubNickname, dto.getKey()).or()
      .like(StrUtil.isNotEmpty(dto.getKey()), Comment::getParentUserId, dto.getKey()).or()
      .like(StrUtil.isNotEmpty(dto.getKey()), Comment::getProductId, dto.getKey()).or()
      .like(StrUtil.isNotEmpty(dto.getKey()), Comment::getContent, dto.getKey())
      .orderByDesc(Comment::getCreateTime)
      .page(new Page<>(dto.getPageNumber(), dto.getPageSize()));
    return page;
  }

  /**
   * 获取评论列表
   * @return
   */
  @Override
  public R getComment() {
    String userId = StpUtil.getLoginIdAsString();
    List<String> productIds = productInfoService.query().eq(USER_ID, userId).list()
            .stream().map(ProductInfo::getId).collect(Collectors.toList());
    if (productIds.isEmpty()){
      return R.ok();
    }
    int noReadCount;
    List<Comment> commentList = query().in(PRODUCT_ID, productIds).ne(PUB_USER_ID, userId).list();
    List<Comment> commentList1 = query().eq(PARENT_USER_ID, userId).list();
    if (!commentList1.isEmpty()){
      commentList.addAll(commentList1);
    }
    noReadCount = (int) commentList.stream().filter(comment -> comment.getIsRead() == 0).count();
    if (commentList.isEmpty()){
      return R.ok();
    }
    List<CommentVo> commentVoList = commentList.stream().map(comment -> {
      String avatar = userService.query().eq(ID, comment.getPubUserId()).one().getAvatar();
      String productImages = productInfoService.query().eq(ID, comment.getProductId()).one().getImage();
      String s = productImages.split(",")[0];
      String productImage = s.substring(2, s.length() - 2);
      CommentVo commentVo = BeanUtil.copyProperties(comment, CommentVo.class);
      commentVo.setPubAvatar(avatar);
      commentVo.setProductImage(productImage);
      commentVo.setNoReadCount(noReadCount);
      if (commentVo.getParentId() != null){
        String parentContent = query().eq(ID, commentVo.getParentId()).one().getContent();
        commentVo.setParentContent(parentContent);
      }
        return commentVo;
    }).sorted((o1, o2) -> (int) (o2.getCreateTime() - o1.getCreateTime())).collect(Collectors.toList());
      return R.ok(commentVoList);
  }
}
