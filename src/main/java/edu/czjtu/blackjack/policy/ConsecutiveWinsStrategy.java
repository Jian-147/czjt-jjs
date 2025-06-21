package edu.czjtu.blackjack.policy;

import edu.czjtu.blackjack.entity.Card;
import edu.czjtu.blackjack.entity.Player;
import edu.czjtu.blackjack.service.GameStrategy;

// 策略2：连赢6局限制策略
public class ConsecutiveWinsStrategy implements GameStrategy {
    @Override
    public boolean shouldApply(Player player) {
        return player.getConsecutiveWins() >= 6;
    }

    @Override
    public void applyStrategy(Player player, GameState gameState) {
        // 玩家初始两张牌为16点（如10+6）
        gameState.getPlayerCards().clear();
        gameState.addCardToPlayer(new Card("♥", "10"));
        gameState.addCardToPlayer(new Card("♦", "6"));

        // 庄家初始牌为17点（有优势）
        gameState.getDealerCards().clear();
        gameState.addCardToDealer(new Card("♣", "7"));
        gameState.addCardToDealer(new Card("♠", "10"));

        // 重置连赢次数
        player.setConsecutiveWins(0);
    }
}