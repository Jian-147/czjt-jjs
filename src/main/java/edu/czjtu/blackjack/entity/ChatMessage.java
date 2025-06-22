package edu.czjtu.blackjack.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 聊天消息实体类
 */
@Data
@TableName("chat_message")
public class ChatMessage {
    @TableId(type = IdType.AUTO)
    private Integer id; // 消息ID
    private Integer userId; // 发送用户ID
    private String username; // 发送用户名
    private String messageContent; // 聊天消息内容
    private LocalDateTime timestamp; // 消息发送时间
} 