package edu.czjtu.blackjack.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.czjtu.blackjack.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * 聊天消息 Mapper 接口
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {
} 