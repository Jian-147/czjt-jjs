package edu.czjtu.blackjack.interceptors;


import edu.czjtu.blackjack.common.JWTUtil;
import edu.czjtu.blackjack.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取 Token
        String token = request.getHeader("Authorization");

        if (token == null) {
            // Token 为空，返回未登录错误信息
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(Result.error("用户未登录。").toString());
            return false;
        }

        try {
            // 验证 Token 有效性并解析用户信息
            Map<String, Object> claims = JWTUtil.parseToken(token);
            // 可以将用户信息存储到 request 中，供后续处理使用
            request.setAttribute("userId", claims.get("userId"));
            request.setAttribute("username", claims.get("username"));
            return true;
        } catch (Exception e) {
            // Token 验证失败，返回错误信息
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(Result.error(e.getMessage()).toString());
            return false;
        }
    }
}
