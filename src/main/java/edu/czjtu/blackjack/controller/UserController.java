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
}
