package edu.czjtu.blackjack.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.czjtu.blackjack.entity.ChatMessage;
import edu.czjtu.blackjack.mapper.ChatMessageMapper;
import edu.czjtu.blackjack.service.ChatService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 聊天服务实现类
 */
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatService {

    @Resource
    private ChatMessageMapper chatMessageMapper;

    @Override
    public ChatMessage sendMessage(Integer userId, String username, String messageContent) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUserId(userId);
        chatMessage.setUsername(username);
        chatMessage.setMessageContent(messageContent);
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessageMapper.insert(chatMessage); // 使用 BaseMapper 的 insert 方法
        return chatMessage;
    }

    @Override
    public List<ChatMessage> getRecentMessages(int limit) {
        LambdaQueryWrapper<ChatMessage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(ChatMessage::getTimestamp); // 按时间倒序排列
        queryWrapper.last("LIMIT " + limit); // 限制查询数量
        return chatMessageMapper.selectList(queryWrapper);
    }
} 