package com.zyh.market.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.bean.BeanUtil;
import com.zyh.market.R;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.dto.ChatListCreateDto;
import com.zyh.market.entity.ChatList;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.service.ChatListService;
import com.zyh.market.vo.ChatListVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat/list")
@SaCheckLogin
public class ChatListController {
  @Autowired private ChatListService chatListService;

  @PostMapping
  public R<String> create(@RequestBody ChatListCreateDto dto){
   String chatListId =  chatListService.create(dto);
   return R.ok(chatListId);
  }
  @GetMapping("/all")
  public R<List<ChatListVo>> getMyChatList(){
    List<ChatListVo> list =  chatListService.getMyChatList();
    return R.ok(list);
  }
  
  @GetMapping("/one")
  public R<ChatList> getMyChatList(String chatListId) {
    ChatList chatList = chatListService.getById(chatListId);
    if(BeanUtil.isEmpty(chatList)) throw new ServiceException(ResultCode.NotFindError);
    return R.ok(chatList);
  }
  @GetMapping("/noRead/total")
  public R getNoReadTotal(){
    int total = chatListService.getNoReadTotal();
    return R.ok(total);
  }
}
