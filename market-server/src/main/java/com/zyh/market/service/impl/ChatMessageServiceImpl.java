package com.zyh.market.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.entity.ChatList;
import com.zyh.market.entity.ChatMessage;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.mapper.ChatMessageMapper;
import com.zyh.market.service.ChatListService;
import com.zyh.market.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.zyh.market.constants.RedisConstants.CHAT_MESSAGE;

@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {
  @Autowired private StringRedisTemplate redisTemplate;
  @Autowired private ChatListService chatListService;
  
  @Override
  public boolean saveList(List<String> messageList) {
    List<ChatMessage> chatMessages = _getMessageListFromRedis(messageList);
    boolean save = saveBatch(chatMessages);
    if (!save) throw new ServiceException(ResultCode.SaveError);
    ChatMessage chatMessage = chatMessages.get(0);
    ChatList chatList = chatListService.getById(chatMessage.getChatListId());
    chatList.setUpdateTime(new Date().getTime());
    boolean update = chatListService.updateById(chatList);
    if (!update) throw new ServiceException(ResultCode.UpdateError);
    return true;
  }
  
  @Override
  public List<ChatMessage> getChatMessageList(String chatListId) {
    //判断redis中是否还有数据没有保存数据库
    List<ChatMessage> redisMessages = new ArrayList<>();
    ListOperations<String, String> operations = redisTemplate.opsForList();
    List<String> messageList = operations.range(CHAT_MESSAGE + chatListId, 0, -1);
    if(messageList!=null){
      redisMessages = _getMessageListFromRedis(messageList);
    }
    List<ChatMessage> dbMessages = lambdaQuery().eq(ChatMessage::getChatListId, chatListId).orderByAsc(ChatMessage::getSendTime).list();
    dbMessages.addAll(redisMessages);
    dbMessages.sort(Comparator.comparing(ChatMessage::getSendTime));
    return dbMessages;
  }
  
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void updateChatMessageIsRead(String chatListId) {
    //获取redis中数据
    ListOperations<String, String> operations = redisTemplate.opsForList();
    List<String> messageListRedis = operations.range(CHAT_MESSAGE + chatListId, 0, -1);
    if (!messageListRedis.isEmpty()) {
      boolean result = saveList(messageListRedis);
      if (result) {
        redisTemplate.delete(CHAT_MESSAGE + chatListId);
      } else {
        throw new ServiceException(ResultCode.DeleteError);
      }
    }
    //获取数据库中未读数据
    List<ChatMessage> dbMessagesNoRead = lambdaQuery().eq(ChatMessage::getChatListId, chatListId)
      .eq(ChatMessage::getToUserId, StpUtil.getLoginId())
      .eq(ChatMessage::getIsRead, 0)
      .list();
    for (ChatMessage chatMessage : dbMessagesNoRead) {
      chatMessage.setIsRead(1);
      boolean update = updateById(chatMessage);
      if (!update) throw new ServiceException(ResultCode.UpdateError);
    }
  }
  
  public List<ChatMessage> _getMessageListFromRedis(List<String> messageList) {
    ArrayList<ChatMessage> chatMessages = new ArrayList<>();
   if(!messageList.isEmpty()){
     for (String message : messageList) {
       ChatMessage chatMessage = new ChatMessage();
       JSONObject jsonObject = JSON.parseObject(message);
       chatMessage.setChatListId(jsonObject.getString("chatListId"));
       chatMessage.setContent(jsonObject.getString("content"));
       chatMessage.setFromUserId(jsonObject.getString("fromUserId"));
       chatMessage.setToUserId(jsonObject.getString("toUserId"));
       chatMessage.setIsRead(Integer.valueOf(jsonObject.getString("isRead")));
       chatMessage.setSendTime(Long.valueOf(jsonObject.getString("sendTime")));
       chatMessages.add(chatMessage);
     }
   }
    return chatMessages;
  }
}
