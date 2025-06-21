package edu.czjtu.blackjack.policy;

import edu.czjtu.blackjack.entity.Card;

import java.util.*;

// 游戏状态类 - 包含当前游戏的信息
public class GameState {
    private List<Card> playerCards;
    private List<Card> dealerCards;
    private boolean isPlayerTurn;

    public GameState() {
        this.playerCards = new ArrayList<>();
        this.dealerCards = new ArrayList<>();
        this.isPlayerTurn = true;
    }

    // Getters and setters
    public List<Card> getPlayerCards() {
        return playerCards;
    }

    public List<Card> getDealerCards() {
        return dealerCards;
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        isPlayerTurn = playerTurn;
    }

    // 添加牌到玩家或庄家
    public void addCardToPlayer(Card card) {
        playerCards.add(card);
    }

    public void addCardToDealer(Card card) {
        dealerCards.add(card);
    }

    // 计算玩家当前手牌点数
    public int calculatePlayerPoints() {
        return calculatePoints(playerCards);
    }

    // 计算庄家当前手牌点数
    public int calculateDealerPoints() {
        return calculatePoints(dealerCards);
    }

    private int calculatePoints(List<Card> cards) {
        int points = 0;
        int aces = 0;

        for (Card card : cards) {
            points += card.getValue();
            if (card.getRank().equals("A")) {
                aces++;
            }
        }

        // 处理A的特殊规则（A可以是1或11）
        while (points > 21 && aces > 0) {
            points -= 10;
            aces--;
        }

        return points;
    }
}