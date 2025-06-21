package edu.czjtu.blackjack.controller;

import edu.czjtu.blackjack.common.Result;
import edu.czjtu.blackjack.entity.Game;
import edu.czjtu.blackjack.service.GameService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/game")
public class GameController {

    @Resource
    private GameService gameService;

    /**
     * 开始新游戏
     * POST /game/start
     */
    @PostMapping("/start")
    public Result startGame(@RequestBody Map<String, Object> params) {
        try {
            int playerId = Integer.parseInt(params.get("playerId").toString());
            int betAmount = Integer.parseInt(params.get("betAmount").toString());
            
            if (betAmount <= 0) {
                return Result.error("下注金额必须大于0");
            }
            
            Game game = gameService.startGame(playerId, betAmount);
            return Result.success(game);
        } catch (Exception e) {
            return Result.error("开始游戏失败：" + e.getMessage());
        }
    }

    /**
     * 玩家要牌
     * POST /game/hit
     */
    @PostMapping("/hit")
    public Result hit(@RequestBody Map<String, Object> params) {
        try {
            Integer gameId = Integer.parseInt(params.get("gameId").toString());
            Game game = gameService.hit(gameId);
            return Result.success(game);
        } catch (Exception e) {
            return Result.error("要牌失败：" + e.getMessage());
        }
    }

    /**
     * 玩家停牌
     * POST /game/stand
     */
    @PostMapping("/stand")
    public Result stand(@RequestBody Map<String, Object> params) {
        try {
            Integer gameId = Integer.parseInt(params.get("gameId").toString());
            Game game = gameService.stand(gameId);
            return Result.success(game);
        } catch (Exception e) {
            return Result.error("停牌失败：" + e.getMessage());
        }
    }

    /**
     * 获取游戏状态
     * GET /game/{gameId}
     */
    @GetMapping("/{gameId}")
    public Result getGameStatus(@PathVariable Integer gameId) {
        try {
            // 这里需要从GameService中获取游戏状态
            // 由于当前实现使用内存存储，这里返回一个提示
            return Result.success("游戏ID: " + gameId + " 的状态");
        } catch (Exception e) {
            return Result.error("获取游戏状态失败：" + e.getMessage());
        }
    }

}
