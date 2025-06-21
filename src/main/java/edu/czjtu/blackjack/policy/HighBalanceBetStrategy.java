package edu.czjtu.blackjack.policy;

import edu.czjtu.blackjack.entity.Card;
import edu.czjtu.blackjack.entity.Player;
import edu.czjtu.blackjack.service.GameStrategy;

import java.util.*;

// 策略3：高金额玩家下注动态平衡策略
public class HighBalanceBetStrategy implements GameStrategy {
    private static final double HIGH_BALANCE_THRESHOLD = 10000;
    // private static final double LOW_BET_THRESHOLD = 50; // 已移除，不再根据单次投注金额区分

    @Override
    public boolean shouldApply(Player player) {
        return player.getBalance() >= HIGH_BALANCE_THRESHOLD;
    }

    @Override
    public void applyStrategy(Player player, GameState gameState) {
        // double currentBet = player.getRecentBets().get(player.getRecentBets().size() - 1); // 已移除

        // 高余额玩家始终按"大额投注"策略处理，使其难以获胜
        int currentPoints = gameState.calculatePlayerPoints();

        // 如果是初始发牌，给16点
        if (gameState.getPlayerCards().size() == 0) {
            gameState.addCardToPlayer(new Card("♥", "10"));
            gameState.addCardToPlayer(new Card("♦", "6"));
        } else if (currentPoints < 21) {
            // 玩家要牌时，发一张牌使其爆牌
            int pointsToBust = 22 - currentPoints;
            if (pointsToBust > 0 && pointsToBust <= 11) {
                String rank = getRankForPoints(pointsToBust);
                gameState.addCardToPlayer(new Card("♥", rank));
            }
        }

        // 庄家牌正常发 (保持原庄家牌策略，不进行特殊控制)
        if (gameState.getDealerCards().size() == 0) {
            gameState.getDealerCards().clear();
            gameState.addCardToDealer(new Card("♣", "7"));
            gameState.addCardToDealer(new Card("♠", "8"));
        }
    }

    private String getRankForPoints(int points) {
        Random random = new Random();
        switch (points) {
            case 1:
                return "A";
            case 10:
                // 随机返回10、J、Q、K中的一个
                String[] tenRanks = { "10", "J", "Q", "K" };
                return tenRanks[random.nextInt(tenRanks.length)];
            default:
                return String.valueOf(points);
        }
    }
}