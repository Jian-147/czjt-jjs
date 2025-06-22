package edu.czjtu.blackjack.service;

import edu.czjtu.blackjack.entity.Player;

public interface GamePolicyStrategy {
    boolean match(Player player);
    int getPolicyCode();
} 