package edu.czjtu.blackjack.policy;

import java.util.*;

// 代理类，引用其他组员创建的类中的胜率属性
public class Yinyog {
    // 模拟存储玩家胜率的映射
    private Map<String, Double> winRates = new HashMap<>();

    /**
     * 获取玩家胜率
     * 
     * @param playerId 玩家ID
     * @return 玩家胜率
     */
    public double getWinRate(String playerId) {
        return winRates.getOrDefault(playerId, 0.5); // 默认返回50%胜率
    }

    /**
     * 设置玩家胜率
     * 
     * @param playerId 玩家ID
     * @param winRate  胜率
     */
    public void setWinRate(String playerId, double winRate) {
        winRates.put(playerId, winRate);
    }
}