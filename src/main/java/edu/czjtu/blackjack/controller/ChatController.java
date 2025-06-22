package edu.czjtu.blackjack.controller;

import edu.czjtu.blackjack.common.Result;
import edu.czjtu.blackjack.entity.ChatMessage;
import edu.czjtu.blackjack.service.ChatService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 聊天功能控制器
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    private ChatService chatService;

    /**
     * 发送聊天消息接口
     * 要求用户已登录
     * @param params 包含 messageContent 的 Map
     * @param request HttpServletRequest 用于获取用户信息
     * @return 结果对象
     */
    @PostMapping("/send")
    public Result sendMessage(@RequestBody Map<String, String> params, HttpServletRequest request) {
        String messageContent = params.get("messageContent");
        if (messageContent == null || messageContent.trim().isEmpty()) {
            return Result.error("消息内容不能为空");
        }

        // 从请求属性中获取用户ID和用户名 (由 LoginInterceptor 设置)
        Integer userId = (Integer) request.getAttribute("userId");
        String username = (String) request.getAttribute("username");

        if (userId == null || username == null) {
            return Result.error("用户未登录或用户信息获取失败");
        }

        ChatMessage sentMessage = chatService.sendMessage(userId, username, messageContent);
        return Result.success(sentMessage);
    }

    /**
     * 获取最近的聊天消息列表接口
     * 要求用户已登录
     * @param limit 获取消息的数量限制，默认为50条
     * @return 结果对象，包含消息列表
     */
    @GetMapping("/messages")
    public Result getRecentMessages(@RequestParam(defaultValue = "50") int limit, HttpServletRequest request) {
        // 检查用户是否登录 (通过 LoginInterceptor 判断)
        // 虽然这里没有直接使用 userId 和 username，但 LoginInterceptor 会确保用户已登录
        if (request.getAttribute("userId") == null) {
            return Result.error("用户未登录");
        }
        
        List<ChatMessage> messages = chatService.getRecentMessages(limit);
        return Result.success(messages);
    }
} 