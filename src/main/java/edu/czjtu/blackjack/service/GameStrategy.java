package edu.czjtu.blackjack.service;

import edu.czjtu.blackjack.policy.GameState;
import edu.czjtu.blackjack.entity.Player;

// 策略接口
public interface GameStrategy {
    boolean shouldApply(Player player);

    void applyStrategy(Player player, GameState gameState);
}