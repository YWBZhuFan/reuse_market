package com.zyh.market.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.market.dto.ChatListCreateDto;
import com.zyh.market.entity.ChatList;
import com.zyh.market.vo.ChatListVo;

import java.util.List;


public interface ChatListService extends IService<ChatList> {
  String create(ChatListCreateDto dto);
  
  List<ChatListVo> getMyChatList();
  
  
  int getNoReadTotal();
  
}
