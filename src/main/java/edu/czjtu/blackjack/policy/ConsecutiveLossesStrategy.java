package edu.czjtu.blackjack.policy;

import edu.czjtu.blackjack.entity.Player;

// 策略3：连输3局保护策略
public class ConsecutiveLossesStrategy implements GamePolicyStrategy {
    
    public ConsecutiveLossesStrategy() {
        // 无参构造函数
    }

    @Override
    public boolean match(Player player) {
        return player.getConsecutiveLosses() >= 3;
    }

    @Override
    public int getPolicyCode() {
        return 3; // 为连输策略分配策略代码 3
    }

}