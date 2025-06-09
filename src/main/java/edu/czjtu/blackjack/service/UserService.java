package edu.czjtu.blackjack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.czjtu.blackjack.entity.User;

public interface UserService extends IService<User> {
    // 注册（简化版）
    User register(String username, String password);
    
    // 登录
    User login(String username, String password);
}
