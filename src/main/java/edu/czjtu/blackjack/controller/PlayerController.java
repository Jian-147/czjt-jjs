package edu.czjtu.blackjack.controller;

import edu.czjtu.blackjack.common.Result;
import edu.czjtu.blackjack.entity.Player;
import edu.czjtu.blackjack.service.PlayerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Resource
    private PlayerService playerService;

    @GetMapping("/info/{playerId}")
    public Result getPlayerInfo(@PathVariable Integer playerId) {
        try {
            Player player = playerService.getPlayerById(playerId);
            if (player == null) {
                return Result.error("玩家不存在");
            }
            return Result.success(player);
        } catch (Exception e) {
            return Result.error("获取玩家信息失败：" + e.getMessage());
        }
    }

    @PostMapping("/updateAllTime")
    public Result updateAllTime(@RequestBody Map<String, Object> params) {
        try {
            Integer playerId = Integer.parseInt(params.get("playerId").toString());
            Integer addedTime = Integer.parseInt(params.get("addedTime").toString());

            playerService.updatePlayerAllTime(playerId, addedTime);
            return Result.success("玩家在线时长更新成功");
        } catch (NumberFormatException e) {
            return Result.error("参数格式不正确：" + e.getMessage());
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("更新玩家在线时长失败：" + e.getMessage());
        }
    }
} 