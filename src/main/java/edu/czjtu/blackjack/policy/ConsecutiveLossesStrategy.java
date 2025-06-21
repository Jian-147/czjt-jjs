package edu.czjtu.blackjack.policy;

import edu.czjtu.blackjack.entity.Card;
import edu.czjtu.blackjack.entity.Player;
import edu.czjtu.blackjack.service.GameStrategy;

// 策略1：连输3局保护策略
public class ConsecutiveLossesStrategy implements GameStrategy {
    @Override
    public boolean shouldApply(Player player) {
        return player.getConsecutiveLosses() >= 3;
    }

    @Override
    public void applyStrategy(Player player, GameState gameState) {
        // 玩家初始两张牌为19点（如10+9）
        gameState.getPlayerCards().clear();
        gameState.addCardToPlayer(new Card("♥", "10"));
        gameState.addCardToPlayer(new Card("♦", "9"));

        // 庄家初始牌为16点
        gameState.getDealerCards().clear();
        gameState.addCardToDealer(new Card("♣", "6"));
        gameState.addCardToDealer(new Card("♠", "10"));

        // 重置连输次数
        player.setConsecutiveLosses(0);
    }
}