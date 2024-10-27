package com.zyh.market.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.market.constants.ResultCode;
import com.zyh.market.dto.ChatListCreateDto;
import com.zyh.market.entity.ChatList;
import com.zyh.market.entity.ChatMessage;
import com.zyh.market.entity.Comment;
import com.zyh.market.entity.User;
import com.zyh.market.exception.ServiceException;
import com.zyh.market.mapper.ChatListMapper;
import com.zyh.market.service.ChatListService;
import com.zyh.market.service.ChatMessageService;
import com.zyh.market.service.CommentService;
import com.zyh.market.service.UserService;
import com.zyh.market.vo.ChatListVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.zyh.market.constants.RedisConstants.CHAT_MESSAGE;

@Service
@Slf4j
public class ChatListServicelmpl extends ServiceImpl<ChatListMapper, ChatList> implements ChatListService {

  @Autowired private UserService userService;
  @Autowired private StringRedisTemplate redisTemplate;
  @Autowired private ChatMessageService chatMessageService;
  
  @Override
  @Transactional(rollbackFor = Exception.class)
  public String create(ChatListCreateDto dto) {
    dto.setFromUserId(StpUtil.getLoginIdAsString());
    //存在则返回
    ChatList one = lambdaQuery().eq(ChatList::getProductId, dto.getProductId())
      .and(wrapper -> {
        wrapper.eq(ChatList::getFromUserId, dto.getFromUserId()).eq(ChatList::getToUserId, dto.getToUserId())
          .or().eq(ChatList::getFromUserId, dto.getToUserId()).eq(ChatList::getToUserId, dto.getFromUserId());
      })
      .one();
    if (!BeanUtil.isEmpty(one)) {
      return one.getId();
    }
    ChatList chatList = new ChatList();
    chatList.setFromUserId(dto.getFromUserId());
    chatList.setToUserId(dto.getToUserId());
    User fromUser = userService.getById(dto.getFromUserId());
    chatList.setFromUserNick(fromUser.getNickName());
    chatList.setFromUserAvatar(fromUser.getAvatar());
    User toUser = userService.getById(dto.getToUserId());
    chatList.setToUserNick(toUser.getNickName());
    chatList.setToUserAvatar(toUser.getAvatar());
    chatList.setProductId(dto.getProductId());
    chatList.setProductImage(dto.getProductImage());
    chatList.setCreateTime(new Date().getTime());
    chatList.setUpdateTime(new Date().getTime());
    boolean save = save(chatList);
    if (!save) throw new ServiceException(ResultCode.SaveError);
    ChatMessage chatMessage = new ChatMessage();
    chatMessage.setChatListId(chatList.getId());
    chatMessage.setContent("我刚刚查看了您的宝贝哦～");
    chatMessage.setFromUserId(StpUtil.getLoginIdAsString());
    chatMessage.setFromUserNick(fromUser.getNickName());
    chatMessage.setToUserId(dto.getToUserId());
    chatMessage.setToUserNick(toUser.getNickName());
    chatMessage.setIsRead(0);
    chatMessage.setSendTime(new Date().getTime());
    boolean save1 = chatMessageService.save(chatMessage);
    if (!save1) throw new ServiceException(ResultCode.SaveError);
    
    return chatList.getId();
  }
  
  @Override
  public List<ChatListVo> getMyChatList() {
    String userId = StpUtil.getLoginIdAsString();
    ArrayList<ChatListVo> chatListVos = new ArrayList<>();
    List<ChatList> chatLists = lambdaQuery().eq(ChatList::getFromUserId, userId).or().eq(ChatList::getToUserId, userId)
      .orderByDesc(ChatList::getUpdateTime).list();
    for (ChatList chatList : chatLists) {
      ChatListVo vo = _getLastMessage(chatList);
      Integer noReadCount = _getNoReadCount(chatList.getId());
      vo.setNoReadCount(noReadCount);
      chatListVos.add(vo);
    }
    return chatListVos;
  }
  
  @Override
  public int getNoReadTotal() {
    int noReadTotal = 0;
    String userId = StpUtil.getLoginIdAsString();
    List<ChatList> chatLists = lambdaQuery().eq(ChatList::getFromUserId, userId).or().eq(ChatList::getToUserId, userId)
      .orderByDesc(ChatList::getUpdateTime).list();
    for (ChatList chatList : chatLists) {
      int noReadCount = _getNoReadCount(chatList.getId());
      noReadTotal += noReadCount;
    }
    return noReadTotal;
  }
  
  private int _getNoReadCount(String chatListId) {
    int noRead = 0;
    ListOperations<String, String> operations = redisTemplate.opsForList();
    List<String> messageList = operations.range(CHAT_MESSAGE + chatListId, 0, -1);
    for (String redisMessage : messageList) {
      JSONObject jsonObject = JSON.parseObject(redisMessage);
      if ("0".equals(jsonObject.getString("isRead")) && jsonObject.getString("toUserId").equals(StpUtil.getLoginIdAsString())) {
        noRead += 1;
      }
    }
    List<ChatMessage> list = chatMessageService.lambdaQuery()
      .eq(ChatMessage::getChatListId, chatListId)
      .eq(ChatMessage::getToUserId, StpUtil.getLoginIdAsString())
      .eq(ChatMessage::getIsRead, 0).list();
    noRead += list.size();
    return noRead;
  }
  
  private ChatListVo _getLastMessage(ChatList chatList) {
    ChatListVo vo = BeanUtil.toBean(chatList, ChatListVo.class);
    ListOperations<String, String> operations = redisTemplate.opsForList();
    List<String> messageList = operations.range(CHAT_MESSAGE + chatList.getId(), 0, -1);
    if (!messageList.isEmpty()) {
      String lastMessage = redisTemplate.opsForList().index(CHAT_MESSAGE + chatList.getId(), messageList.size() - 1);
      ChatMessage chatMessage = _convertMessage(lastMessage);
      vo.setChatMessage(chatMessage);
    } else {
      ChatMessage message = chatMessageService.lambdaQuery().eq(ChatMessage::getChatListId, chatList.getId())
        .orderByDesc(ChatMessage::getSendTime)
        .last("limit 0,1").one();
      if (BeanUtil.isEmpty(message)) {
        vo.setChatMessage(new ChatMessage());
      } else {
        vo.setChatMessage(message);
      }
    }
    return vo;
  }
  
  private ChatMessage _convertMessage(String message) {
    ChatMessage chatMessage = new ChatMessage();
    JSONObject jsonObject = JSON.parseObject(message);
    chatMessage.setChatListId(jsonObject.getString("chatListId"));
    chatMessage.setContent(jsonObject.getString("content"));
    chatMessage.setFromUserId(jsonObject.getString("fromUserId"));
    chatMessage.setToUserId(jsonObject.getString("toUserId"));
    chatMessage.setIsRead(Integer.valueOf(jsonObject.getString("isRead")));
    chatMessage.setSendTime(Long.valueOf(jsonObject.getString("sendTime")));
    return chatMessage;
  }
}
