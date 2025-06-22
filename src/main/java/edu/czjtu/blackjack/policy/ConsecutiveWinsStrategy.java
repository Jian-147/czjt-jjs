package edu.czjtu.blackjack.policy;

import edu.czjtu.blackjack.entity.Player;

// 策略4：连赢6局限制策略
public class ConsecutiveWinsStrategy implements GamePolicyStrategy {
    
    public ConsecutiveWinsStrategy() {
        // 无参构造函数
    }

    @Override
    public boolean match(Player player) {
        return player.getConsecutiveWins() >= 6;
    }

    @Override
    public int getPolicyCode() {
        return 4; // 为连赢策略分配策略代码 4
    }

}