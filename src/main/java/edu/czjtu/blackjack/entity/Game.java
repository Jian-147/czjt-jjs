package edu.czjtu.blackjack.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    private Integer gameId;           // 游戏ID
    private Integer playerId;         // 玩家ID
    private String playerCards;       // 玩家手牌
    private String dealerCards;       // 庄家手牌
    private Integer playerScore;      // 玩家点数
    private Integer dealerScore;      // 庄家点数
    private String gameStatus;        // 游戏状态：进行中、玩家赢、庄家赢、平局
    private Integer betAmount;        // 下注金额
    private String result;            // 游戏结果描述
} 