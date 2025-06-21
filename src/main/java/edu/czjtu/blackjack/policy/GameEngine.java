package edu.czjtu.blackjack.policy;

import edu.czjtu.blackjack.entity.Card;
import edu.czjtu.blackjack.entity.Player;

import java.util.*;

// 游戏引擎类 - 处理游戏逻辑
public class GameEngine {
    private GameStrategyManager strategyManager;

    public GameEngine(Yinyog yinyog) {
        // 初始化策略管理器时传入Yinyog实例
        this.strategyManager = new GameStrategyManager(yinyog);
    }

    // 开始新游戏
    public GameState startGame(Player player, double bet) {
        // 更新玩家下注信息
        player.addBet(bet);

        // 创建新游戏状态
        GameState gameState = new GameState();

        // 检查是否需要应用策略
        boolean strategyApplied = strategyManager.applyStrategyIfNeeded(player, gameState);

        // 如果没有应用策略，进行正常发牌
        if (!strategyApplied) {
            dealInitialCards(gameState);
        }

        return gameState;
    }

    // 正常发牌逻辑
    private void dealInitialCards(GameState gameState) {
        // 玩家初始两张随机牌
        gameState.addCardToPlayer(getRandomCard());
        gameState.addCardToPlayer(getRandomCard());

        // 庄家初始两张随机牌
        gameState.addCardToDealer(getRandomCard());
        gameState.addCardToDealer(getRandomCard());
    }

    // 生成随机牌
    private Card getRandomCard() {
        Random random = new Random();
        String[] suits = {"♠", "♥", "♣", "♦"};
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

        return new Card(
                suits[random.nextInt(suits.length)],
                ranks[random.nextInt(ranks.length)]);
    }

    // 玩家要牌
    public void playerHit(GameState gameState) {
        if (gameState.isPlayerTurn()) {
            gameState.addCardToPlayer(getRandomCard());
        }
    }

    // 玩家停止要牌，庄家行动
    public void playerStand(GameState gameState) {
        if (gameState.isPlayerTurn()) {
            gameState.setPlayerTurn(false);

            // 庄家按规则行动
            while (gameState.calculateDealerPoints() < 17) {
                gameState.addCardToDealer(getRandomCard());
            }
        }
    }

    // 判定游戏结果
    public GameResult determineResult(GameState gameState) {
        int playerPoints = gameState.calculatePlayerPoints();
        int dealerPoints = gameState.calculateDealerPoints();

        if (playerPoints > 21) {
            return GameResult.PLAYER_BUST;
        } else if (dealerPoints > 21) {
            return GameResult.DEALER_BUST;
        } else if (playerPoints > dealerPoints) {
            return GameResult.PLAYER_WIN;
        } else if (playerPoints < dealerPoints) {
            return GameResult.DEALER_WIN;
        } else {
            return GameResult.TIE;
        }
    }

    // 更新玩家状态
    public void updatePlayerState(Player player, GameResult result, double bet) {
        switch (result) {
            case PLAYER_WIN:
                player.processWin(bet);
                break;
            case DEALER_WIN:
            case PLAYER_BUST:
                player.processLoss(bet);
                break;
            case DEALER_BUST:
                player.processWin(bet);
                break;
            case TIE:
                // 平局不改变余额
                break;
        }
    }
}