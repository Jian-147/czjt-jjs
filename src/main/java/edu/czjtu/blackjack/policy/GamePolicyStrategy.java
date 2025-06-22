package edu.czjtu.blackjack.policy;

import edu.czjtu.blackjack.entity.Player;

public interface GamePolicyStrategy {
    boolean match(Player player);
    int getPolicyCode();
} 