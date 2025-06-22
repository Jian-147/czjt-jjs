package edu.czjtu.blackjack.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.czjtu.blackjack.common.IdGenerator;
import edu.czjtu.blackjack.entity.Player;
import edu.czjtu.blackjack.entity.User;
import edu.czjtu.blackjack.exception.CustomException;
import edu.czjtu.blackjack.mapper.UserMapper;
import edu.czjtu.blackjack.mapper.PlayerMapper;
import edu.czjtu.blackjack.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private PlayerMapper playerMapper;

    @Resource
    private MeterRegistry meterRegistry;

    private Counter successfulLoginsCounter;
    private Counter newRegistrationsCounter;

    @PostConstruct
    public void init() {
        successfulLoginsCounter = Counter.builder("user.successful.logins.total")
                .description("Total number of successful user logins")
                .register(meterRegistry);

        newRegistrationsCounter = Counter.builder("user.new.registrations.total")
                .description("Total number of new user registrations")
                .register(meterRegistry);

        meterRegistry.gauge("user.registered.total", this, UserServiceImpl::getTotalRegisteredUsers);
    }

    private double getTotalRegisteredUsers() {
        return this.count(); // this.count() 继承自 ServiceImpl，用于获取总用户数
    }

    @Override
    public User register(String username, String password) {
        // 1. 参数校验
        if (username == null || username.trim().length() < 3) {
            throw new CustomException("1", "用户名长度不能小于3位！");
        }
        if (password == null || password.length() < 6) {
            throw new CustomException("1", "密码长度不能小于6位！");
        }

        // 2. 检查用户名是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        if (count(queryWrapper) > 0) {
            throw new CustomException("1", "用户名已存在！");
        }

        // 3. 生成不重复的ID
        Integer userId;
        do {
            userId = IdGenerator.generateUserId();
        } while (isIdExists(userId));

        // 4. 创建用户对象并保存到用户表
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        // 密码加密存储
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));
        user.setEmail(""); // 默认为空
        user.setPhone(""); // 默认为空
        user.setUserPic("/userpic"); // 默认头像
        user.setBalance(2000); // 初始余额为0
        user.setState("离线"); // 初始状态为离线
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        save(user);
        newRegistrationsCounter.increment(); // increment counter

        // 5. 同时创建对应的Player记录并保存到玩家表
        Player player = new Player();
        player.setPlayerId(userId); // 使用User的ID作为Player的ID
        player.setBalance(2000.0); // 初始游戏余额，可根据需求调整
        player.setInitialBalance(2000.0); // 初始游戏余额
        player.setConsecutiveLosses(0);
        player.setConsecutiveWins(0);
        player.setTotalLoss(0.0);
        player.setWinNumber(0);
        player.setAllNumber(0);
        player.setWinning(0);
        player.setWin21(0);
        player.setAllTime(0);
        player.setDan("新手III"); // 默认段位
        player.setDanNumber(0); // 默认段位积分
        player.setDanSort("未上榜"); // 默认段位排名
        playerMapper.insert(player);

        return user;
    }

    @Override
    public User login(String username, String password) {
        // 1. 参数校验
        if (username == null || username.trim().isEmpty()) {
            throw new CustomException("1", "用户名不能为空！");
        }
        if (password == null || password.isEmpty()) {
            throw new CustomException("1", "密码不能为空！");
        }

        // 2. 查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = getOne(queryWrapper);

        // 3. 验证用户存在性和密码
        if (user == null) {
            throw new CustomException("1", "用户名或密码错误！");
        }
        
        // 密码加密后比较
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!user.getPassword().equals(encryptedPassword)) {
            throw new CustomException("1", "用户名或密码错误！");
        }

        // 4. 更新用户状态为在线
        user.setState("在线");
        user.setUpdateTime(LocalDateTime.now());
        updateById(user);
        successfulLoginsCounter.increment(); // increment counter

        // 5. 返回用户信息（不包含密码）
        user.setPassword(null);
        return user;
    }

    private boolean isIdExists(Integer id) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, id);
        return count(queryWrapper) > 0;
    }
}
