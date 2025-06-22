package edu.czjtu.blackjack.policy;

import edu.czjtu.blackjack.entity.Player;

import java.util.*;

// 策略1：高胜率玩家控制策略
public class HighWinRateStrategy implements GamePolicyStrategy {
    private static final double HIGH_WIN_RATE_THRESHOLD = 0.7; // 高胜率阈值（70%）
    private static final double LOW_WIN_RATE_THRESHOLD = 0.65; // 低胜率阈值（65%）

    private final Yinyog yinyog;
    private final Random random = new Random();

    // 为 Policy.java 提供无参构造函数
    public HighWinRateStrategy() {
        this.yinyog = new Yinyog(); // 假设 Yinyog 有无参构造函数
    }

    public HighWinRateStrategy(Yinyog yinyog) {
        this.yinyog = yinyog;
    }

    /**
     * 判断是否应该应用此策略
     * 
     * @param player 玩家状态
     * @return true-玩家胜率≥70%，false-否则
     */
    @Override
    public boolean match(Player player) {
        // 假设 Yinyog.getWinRate 是一个静态方法或可以通过无参构造函数访问
        double winRate = new Yinyog().getWinRate(String.valueOf(player.getPlayerId()));
        return player.getAllNumber() > 0 && winRate >= HIGH_WIN_RATE_THRESHOLD;
    }

    @Override
    public int getPolicyCode() {
        return 1; // 为高胜率策略分配策略代码 1
    }

}