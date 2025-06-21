package edu.czjtu.blackjack.controller;

import edu.czjtu.blackjack.common.JWTUtil;
import edu.czjtu.blackjack.common.Result;
import edu.czjtu.blackjack.entity.User;
import edu.czjtu.blackjack.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Result register(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        
        User user = userService.register(username, password);
        return Result.success(user);
    }

    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        
        User user = userService.login(username, password);

        //生成JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        String token = JWTUtil.generateToken(claims);

        //将 token 放入响应数据中
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("user", user);
        responseData.put("token", token);
        return Result.success(responseData);
    }

    @GetMapping("/list")
    public Result list() {
        List<User> users = userService.list();
        return Result.success(users);
    }

    @PostMapping("/logout")
    public Result logout(@RequestHeader(value = "Authorization", required = false) String token,
                         @RequestBody Map<String, Object> params) {
        Object idObj = params.get("id");
        if (idObj == null) {
            return Result.error("用户id不能为空");
        }
        int id;
        try {
            id = Integer.parseInt(idObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("用户id格式不正确");
        }
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        //调用JWTUtil,删除Redis中Token记录
        if (token != null) {
            JWTUtil.deleteToken(token);
        }
        //设置用户状态为离线并更新
        user.setState("离线");
        userService.updateById(user);
        // ThreadLocalUtil 是我的自定义的工具类,暂时还没有
        // ThreadLocalUtil.clear();
        return Result.success("退出登录成功");
    }

    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Integer id) {
        boolean success = userService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    @PostMapping("/update")
    public Result updateUser(@RequestBody User user) {
        try {
            // 根据用户ID查询用户是否存在
            User existingUser = userService.getById(user.getId());
            if (existingUser == null) {
                return Result.error("用户不存在");
            }
            // 更新用户信息
            if (user.getUsername() != null) {
                existingUser.setUsername(user.getUsername());
            }
            if (user.getPassword() != null) {
                // 密码加密存储
                existingUser.setPassword(org.springframework.util.DigestUtils.md5DigestAsHex(user.getPassword().getBytes(java.nio.charset.StandardCharsets.UTF_8)));
            }
            if (user.getEmail() != null) {
                existingUser.setEmail(user.getEmail());
            }
            if (user.getPhone() != null) {
                existingUser.setPhone(user.getPhone());
            }
            if (user.getUserPic() != null) {
                existingUser.setUserPic(user.getUserPic());
            }
            if (user.getBalance() != null) {
                existingUser.setBalance(user.getBalance());
            }
            if (user.getState() != null) {
                existingUser.setState(user.getState());
            }
            existingUser.setUpdateTime(java.time.LocalDateTime.now());

            // 保存更新后的用户信息
            boolean success = userService.updateById(existingUser);
            if (success) {
                return Result.success("用户信息更新成功");
            } else {
                return Result.error("用户信息更新失败");
            }
        } catch (Exception e) {
            return Result.error("更新用户信息时发生错误：" + e.getMessage());
        }
    }
    @PostMapping("/updateBalance")
    public Result updateBalance(@RequestBody Map<String, Object> params) {
        try {
            // 获取用户ID
            Object idObj = params.get("id");
            if (idObj == null) {
                return Result.error("用户ID不能为空");
            }
            int id;
            try {
                id = Integer.parseInt(idObj.toString());
            } catch (NumberFormatException e) {
                return Result.error("用户ID格式不正确");
            }

            // 获取要修改的余额
            Object balanceObj = params.get("balance");
            if (balanceObj == null) {
                return Result.error("余额不能为空");
            }
            int balance;
            try {
                balance = Integer.parseInt(balanceObj.toString());
                if (balance < 0) {
                    return Result.error("余额不能为负数");
                }
            } catch (NumberFormatException e) {
                return Result.error("余额格式不正确");
            }

            // 查询用户是否存在
            User existingUser = userService.getById(id);
            if (existingUser == null) {
                return Result.error("用户不存在");
            }

            // 更新余额
            existingUser.setBalance(balance);
            existingUser.setUpdateTime(java.time.LocalDateTime.now());

            // 保存更新
            boolean success = userService.updateById(existingUser);
            if (success) {
                return Result.success("余额更新成功");
            } else {
                return Result.error("余额更新失败");
            }
        } catch (Exception e) {
            return Result.error("更新余额时发生错误：" + e.getMessage());
        }
    }
}
