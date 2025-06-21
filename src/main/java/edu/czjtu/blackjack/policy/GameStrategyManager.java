package edu.czjtu.blackjack.policy;

import edu.czjtu.blackjack.entity.Player;
import edu.czjtu.blackjack.service.GameStrategy;

import java.util.*;

// 策略管理器
public class GameStrategyManager {
    private List<GameStrategy> strategies;

    public GameStrategyManager(Yinyog yinyog) {
        this.strategies = new ArrayList<>();
        // 初始化所有策略
        strategies.add(new ConsecutiveLossesStrategy());
        strategies.add(new ConsecutiveWinsStrategy());
        strategies.add(new HighBalanceBetStrategy());
        // 使用新的策略4（基于胜率）
        strategies.add(new HighWinRateStrategy(yinyog));
    }

    // 应用合适的策略
    public boolean applyStrategyIfNeeded(Player player, GameState gameState) {
        for (GameStrategy strategy : strategies) {
            if (strategy.shouldApply(player)) {
                strategy.applyStrategy(player, gameState);
                return true; // 已应用策略
            }
        }
        return false; // 未应用任何策略
    }

    // 添加自定义策略
    public void addStrategy(GameStrategy strategy) {
        strategies.add(strategy);
    }

    // 获取所有策略
    public List<GameStrategy> getStrategies() {
        return strategies;
    }
}