package edu.czjtu.blackjack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.czjtu.blackjack.entity.ChatMessage;

import java.util.List;

/**
 * 聊天服务接口
 */
public interface ChatService extends IService<ChatMessage> {
    /**
     * 发送一条聊天消息
     * @param userId 发送用户ID
     * @param username 发送用户名
     * @param messageContent 消息内容
     * @return 发送成功的消息对象
     */
    ChatMessage sendMessage(Integer userId, String username, String messageContent);

    /**
     * 获取最近的聊天消息列表
     * @param limit 获取消息的数量限制
     * @return 聊天消息列表，按时间倒序排列
     */
    List<ChatMessage> getRecentMessages(int limit);
} 