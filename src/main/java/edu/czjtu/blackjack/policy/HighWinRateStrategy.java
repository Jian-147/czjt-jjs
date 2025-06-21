package edu.czjtu.blackjack.policy;

import edu.czjtu.blackjack.entity.Card;
import edu.czjtu.blackjack.entity.Player;
import edu.czjtu.blackjack.service.GameStrategy;

import java.util.*;

// 策略4：高胜率玩家控制策略
public class HighWinRateStrategy implements GameStrategy {
    private static final double HIGH_WIN_RATE_THRESHOLD = 0.7; // 高胜率阈值（70%）
    private static final double LOW_WIN_RATE_THRESHOLD = 0.65; // 低胜率阈值（65%）

    // 引用其他组员创建的类中的胜率属性
    private final Yinyog yinyog;
    private final Random random = new Random(); // 添加Random实例

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
    public boolean shouldApply(Player player) {
        double winRate = yinyog.getWinRate(String.valueOf(player.getPlayerId()));
        return winRate >= HIGH_WIN_RATE_THRESHOLD;
    }

    /**
     * 应用策略，控制高胜率玩家的牌面
     * 
     * @param player    玩家状态
     * @param gameState 当前游戏状态
     */
    @Override
    public void applyStrategy(Player player, GameState gameState) {
        List<Card> playerCards = gameState.getPlayerCards();
        double winRate = yinyog.getWinRate(String.valueOf(player.getPlayerId()));

        if (winRate < LOW_WIN_RATE_THRESHOLD) {
            return; // 胜率已低于65%，不干预
        }

        if (playerCards.size() == 0) {
            // 初始两张牌控制为12-15点
            int targetPoints = 12 + random.nextInt(4); // 12-15随机

            // 分解目标点数为两张牌
            int firstCardValue = 2 + random.nextInt(10); // 2-11
            int secondCardValue = targetPoints - firstCardValue;

            // 确保第二张牌值在合理范围内
            if (secondCardValue < 2) {
                secondCardValue = 2;
                firstCardValue = targetPoints - secondCardValue;
            } else if (secondCardValue > 11) {
                secondCardValue = 11;
                firstCardValue = targetPoints - secondCardValue;
            }

            // 添加两张牌
            gameState.addCardToPlayer(createCardWithValue(firstCardValue));
            gameState.addCardToPlayer(createCardWithValue(secondCardValue));
        } else if (playerCards.size() == 2) {
            // 玩家第三次要牌时给A
            gameState.addCardToPlayer(new Card("♥", "A")); // 修改：使用字符串表示花色和点数
        } else if (playerCards.size() >= 3) {
            // 玩家第四次及以后要牌时给10、J、Q、K中的一张，使其爆牌
            String[] tenRanks = { "10", "J", "Q", "K" }; // 修改：使用字符串数组
            String selectedRank = tenRanks[random.nextInt(tenRanks.length)]; // 修改：使用字符串
            gameState.addCardToPlayer(new Card("♥", selectedRank)); // 修改：使用字符串表示花色和点数
        }
    }

    /**
     * 创建指定点数的牌
     * 
     * @param value 牌面点数
     * @return 对应点数的牌
     */
    private Card createCardWithValue(int value) {
        switch (value) {
            case 11:
                return new Card("♥", "A"); // 修改：使用字符串表示花色和点数
            case 10:
                String[] tenRanks = { "10", "J", "Q", "K" }; // 修改：使用字符串数组
                return new Card("♥", tenRanks[random.nextInt(tenRanks.length)]); // 修改：使用字符串表示花色和点数
            default:
                return new Card("♥", String.valueOf(value)); // 修改：使用字符串表示点数
        }
    }
}