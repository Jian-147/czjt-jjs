package edu.czjtu.blackjack.policy;

import edu.czjtu.blackjack.entity.Player;


// 策略2：高金额玩家下注动态平衡策略
public class HighBalanceBetStrategy implements GamePolicyStrategy {
    private static final double HIGH_BALANCE_THRESHOLD = 10000;

    public HighBalanceBetStrategy() {
        // 无参构造函数
    }

    @Override
    public boolean match(Player player) {
        return player.getBalance() >= HIGH_BALANCE_THRESHOLD;
    }

    @Override
    public int getPolicyCode() {
        return 2; // 为高金额玩家策略分配策略代码 2
    }

}