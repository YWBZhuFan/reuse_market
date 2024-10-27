package com.zyh.market.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.zyh.market.R;
import com.zyh.market.entity.ChatMessage;

import com.zyh.market.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat/message")
@SaCheckLogin
public class ChatMessageController {
  @Autowired private ChatMessageService chatMessageService;
  
  @GetMapping
  public R<List<ChatMessage>> getChatMessageList(String chatListId) {
    List<ChatMessage> list =  chatMessageService.getChatMessageList(chatListId);
    return R.ok(list);
  }
  @PutMapping("/{chatListId}")
  public R updateChatMessageIsRead(@PathVariable("chatListId") String chatListId){
    chatMessageService.updateChatMessageIsRead(chatListId);
    return R.ok();
  }
}
