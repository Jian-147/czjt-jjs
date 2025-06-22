package edu.czjtu.blackjack.entity;

import edu.czjtu.blackjack.policy.ConsecutiveLossesStrategy;
import edu.czjtu.blackjack.policy.ConsecutiveWinsStrategy;
import edu.czjtu.blackjack.policy.GamePolicyStrategy;
import edu.czjtu.blackjack.policy.HighBalanceBetStrategy;
import edu.czjtu.blackjack.policy.HighWinRateStrategy;

import java.util.Arrays;
import java.util.List;

public class Policy {
    private int policy = 99;

    // 策略列表
    private static final List<GamePolicyStrategy> strategies = Arrays.asList(
        new HighWinRateStrategy(),
        new HighBalanceBetStrategy(),
        new ConsecutiveLossesStrategy(),
        new ConsecutiveWinsStrategy()
        // 您可以在这里添加更多策略
    );

    public void updatePolicy(Player player) {
        this.policy = 99; // 默认策略
        for (GamePolicyStrategy strategy : strategies) {
            if (strategy.match(player)) {
                this.policy = strategy.getPolicyCode();
                break; // 匹配到第一个策略就使用它
            }
        }
    }

    public int getPolicy() {
        return policy;
    }
}
