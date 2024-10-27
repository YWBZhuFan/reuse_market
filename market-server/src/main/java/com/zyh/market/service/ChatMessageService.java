package com.zyh.market.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.market.entity.ChatList;
import com.zyh.market.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService extends IService<ChatMessage> {
  boolean saveList(List<String> messageList);
  
  List<ChatMessage> getChatMessageList(String chatListId);
  
  void updateChatMessageIsRead(String chatListId);
  
}
