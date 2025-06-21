package edu.czjtu.blackjack.service;

import edu.czjtu.blackjack.entity.Game;

public interface GameService {
    // 开始新游戏
    Game startGame(Integer playerId, Integer betAmount);
    
    // 玩家要牌
    Game hit(Integer gameId);
    
    // 玩家停牌
    Game stand(Integer gameId);
    
    // 计算手牌点数
    int calculateScore(java.util.List<edu.czjtu.blackjack.entity.Card> cards);
    
    // 检查是否爆牌
    boolean isBust(int score);
    
    // 检查是否黑杰克
    boolean isBlackJack(java.util.List<edu.czjtu.blackjack.entity.Card> cards);
} 