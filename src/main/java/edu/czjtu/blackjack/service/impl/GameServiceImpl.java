package edu.czjtu.blackjack.service.impl;

import edu.czjtu.blackjack.entity.Card;
import edu.czjtu.blackjack.entity.Game;
import edu.czjtu.blackjack.entity.Player;
import edu.czjtu.blackjack.entity.Policy;
import edu.czjtu.blackjack.service.GameService;
import edu.czjtu.blackjack.mapper.PlayerMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {
    
    private Map<Integer, Game> games = new HashMap<>(); // 存储游戏状态
    private List<Card> deck = new ArrayList<>(); // 牌堆
    private Random random = new Random();

    @Resource
    private PlayerMapper playerMapper;
    
    // 初始化牌堆
    private void initializeDeck() {
        deck.clear();//清空牌库
        String[] suits = {"♠", "♥", "♣", "♦"};
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        //给deck中生成52张牌
        for (int i = 0; i < suits.length; i++) {
            for (int j = 0; j < ranks.length; j++) {
                deck.add(new Card(suits[i], ranks[j]));
            }
        }
    }
    
    // 洗牌
    private void shuffleDeck() {
        Collections.shuffle(deck, random); //使用工具类Collections中的shuffle方法来打乱deck集合中的扑克牌顺序
    }
    
    // 辅助方法：从牌堆中随机获取一张牌并移除
    private Card getRandomCard() {
        if (deck.isEmpty()) {
            initializeDeck();
            shuffleDeck();
        }
        return deck.remove(random.nextInt(deck.size()));
    }

    // 从牌堆中找到并移除一张特定点数的牌
    private Card findAndRemoveSpecificCard(int targetValue) {
        for (int i = 0; i < deck.size(); i++) {
            Card card = deck.get(i);
            // 对于A, J, Q, K，点数是11或10
            int cardActualValue = card.getValue(); // 假设 Card.getValue() 返回数字牌的点数，A为11，JQK为10
            
            if (card.getRank().equals("A")) {
                if (targetValue == 1 || targetValue == 11) return deck.remove(i);
            } else if (("J".equals(card.getRank()) || "Q".equals(card.getRank()) || "K".equals(card.getRank())) && targetValue == 10) {
                return deck.remove(i);
            } else if (cardActualValue == targetValue && cardActualValue >=2 && cardActualValue <=9) {
                return deck.remove(i);
            }
        }
        return getRandomCard(); // 如果没有找到，就发一张随机牌
    }

    // p1 方法：爆牌策略
    private Card p1(List<Card> currentHand) {
        int currentScore = calculateScore(currentHand); // 使用已有的 calculateScore 方法
        // 尝试发一张能使总分超过21的牌
        for (int i = 0; i < deck.size(); i++) {
            Card card = deck.get(i);
            int cardValue = card.getValue(); // 获取牌的实际点数
            if (card.getRank().equals("A")) {
                // 尝试将A视为11点来爆牌
                if (currentScore + 11 > 21) {
                    return deck.remove(i);
                }
            }
            if (currentScore + cardValue > 21) {
                return deck.remove(i);
            }
        }
        // 如果没有牌能直接爆牌，尝试发一张高点数的牌，增加后续爆牌可能性
        return findAndRemoveSpecificCard(10); // 尝试发一张10点牌
    }

    // p2 方法：条件性爆牌策略
    private Card p2(List<Card> currentHand, Player player, int betAmount) {
        if (player != null && betAmount > player.getBalance() / 2.0) {
            return p1(currentHand); // 调用 p1 逻辑实现爆牌
        } else {
            return getRandomCard(); // 否则随机发牌
        }
    }

    // p3 方法：使用21点策略
    private Card p3(List<Card> currentHand) {
        int currentScore = calculateScore(currentHand);
        int needed = 21 - currentScore;

        // 如果已经21点或超过，或不可能通过一张牌达到21点
        if (needed <= 0 || needed > 11) { // 需要的点数不可能通过一张牌达到
            return getRandomCard();
        }

        // 尝试找到精确的点数牌
        Card exactCard = findAndRemoveSpecificCard(needed);
        if (exactCard != null) {
            return exactCard;
        }
        return getRandomCard(); // 如果没找到精确的牌，就随机发牌
    }

    // 用户的发牌方法（使用策略）
    private Card dealCard(Game game, int policyCode, boolean isPlayerCard) {
        if (deck.isEmpty()) {
            initializeDeck();
            shuffleDeck();
        }

        List<Card> targetHand = isPlayerCard ? parseCards(game.getPlayerCards()) : parseCards(game.getDealerCards());
        Player player = playerMapper.selectById(game.getPlayerId()); // 获取最新玩家数据
        int betAmount = game.getBetAmount(); // 获取游戏中的下注金额

        switch (policyCode) {
            case 1: // 高胜率玩家 - 使用爆牌策略
                return p1(targetHand);
            case 2: // 高余额玩家 - 使用条件性爆牌策略
                return p2(targetHand, player, betAmount);
            case 3: // 连败局玩家 - 使用21点策略
                return p3(targetHand);
            case 4: // 连胜玩家 - 使用爆牌策略
                return p1(targetHand); // 连赢限制也用爆牌逻辑
            default:
                return getRandomCard(); // 默认策略：随机发牌
        }
    }
    //庄家策略
    private Card dealERCard() {
        return getRandomCard(); // 默认策略：随机发牌
    }

    //把集合中所有元素转换成字符串
    private String convertCardsToString(List<Card> cards) {
        //CSV 格式（简单拼接）
        Collectors Collectors = null;
        return cards.stream().map(Card::toString).collect(java.util.stream.Collectors.joining(","));
    }
    //把字符串换成集合的形式
    private List<Card> parseCards(String cardStr) {
        List<Card> cards = new ArrayList<>();
        if (cardStr == null || cardStr.isEmpty()) {
            return cards;
        }
        for (String part : cardStr.split(",")) {
            // 假设格式为 "花色+点数"，如 "♠A"
            String suit = part.substring(0, 1);    // 取第一个字符（花色）
            String rank = part.substring(1);       // 取剩余字符（点数）
            cards.add(new Card(suit, rank));      // 创建 Card 对象
        }
        return cards;
    }
    
    @Override//开始新游戏
    public Game startGame(Integer playerId, Integer betAmount) {
        // 初始化牌堆并洗牌
        initializeDeck();
        shuffleDeck();
        //获取发牌策略
        Policy policy = new Policy();
        Player player = playerMapper.selectById(playerId);
        policy.updatePolicy(player);
        int activePolicyCode = policy.getPolicy(); // 获取激活的策略代码

        // 创建新游戏
        Game game = new Game();
        game.setGameId(generateGameId());
        game.setPlayerId(playerId);
        game.setBetAmount(betAmount);
        game.setGameStatus("进行中");

        List<Card> playerCards = new ArrayList<>();
        List<Card> dealerCards = new ArrayList<>();

        // 玩家两张牌
        // 每次发牌前更新游戏对象的牌字符串，以便 dealCard 内部的 parseCards 获取最新手牌
        playerCards.add(dealCard(game, activePolicyCode, true));
        game.setPlayerCards(convertCardsToString(playerCards));
        playerCards.add(dealCard(game, activePolicyCode, true));
        game.setPlayerCards(convertCardsToString(playerCards));

        // 庄家两张牌（第一张明牌，第二张暗牌）
        dealerCards.add(dealERCard()); // 使用 dealERCard()
        game.setDealerCards(convertCardsToString(dealerCards));
        dealerCards.add(dealERCard()); // 使用 dealERCard()
        game.setDealerCards(convertCardsToString(dealerCards));

        // 计算点数
        game.setPlayerScore(calculateScore(playerCards));
        game.setDealerScore(calculateScore(Arrays.asList(dealerCards.get(0))));

        //埋点判断是否21点
        if(game.getPlayerScore()==21){
            player.setWin21(player.getWin21()+1);
            playerMapper.updateById(player);
        }

        // 检查是否黑杰克
        if (isBlackJack(playerCards)) {
            game.setGameStatus("玩家赢");
            game.setResult("黑杰克！玩家获胜！");
            win(game.getPlayerId(), game.getPlayerScore());
            game.setBetAmount(game.getBetAmount()*2);//筹码翻倍

            game.setDealerScore(calculateScore(dealerCards)); // 显示庄家完整点数

        } else if (isBlackJack(dealerCards)) {
            game.setGameStatus("庄家赢");
            game.setResult("庄家黑杰克！庄家获胜！");
            fail(game.getPlayerId());
            game.setBetAmount(0);//筹码归零

            game.setDealerScore(calculateScore(dealerCards)); // 显示庄家完整点数

        }

        // 存储游戏状态
        games.put(game.getGameId(), game);

        // 数据埋点：更新玩家数据
        if (player != null) {
            if ("玩家赢".equals(game.getGameStatus())) {
                player.setWinNumber(player.getWinNumber() + 1); // 增加胜场
                player.setConsecutiveWins(player.getConsecutiveWins() + 1); // 增加连赢次数
                player.setConsecutiveLosses(0); // 连输次数清零
                if (isBlackJack(playerCards)) {
                    player.setWin21(player.getWin21() + 1); // 增加21点次数
                }
            } else if ("庄家赢".equals(game.getGameStatus())) {
                player.setConsecutiveLosses(player.getConsecutiveLosses() + 1); // 增加连输次数
                player.setConsecutiveWins(0); // 连赢次数清零
                player.setTotalLoss(player.getTotalLoss() + game.getBetAmount()); // 累加总亏损（如果庄家黑杰克导致输掉）
            }
            // 重新计算胜率
            if (player.getAllNumber() > 0) {
                player.setWinning((int) ((double) player.getWinNumber() / player.getAllNumber() * 100));
            } else {
                player.setWinning(0);
            }
            playerMapper.updateById(player); // 更新玩家记录
        }

        return game;
    }
    //玩家要牌
    @Override
    public Game hit(Integer gameId) {
        Game game = games.get(gameId);
        if (game == null) {
            throw new RuntimeException("游戏不存在");
        }

        if (!"进行中".equals(game.getGameStatus())) {
            throw new RuntimeException("游戏已结束");
        }
        //获取玩家对象和发牌策略
        Player player = playerMapper.selectById(game.getPlayerId()); // 在方法开头声明一次
        Policy policy = new Policy();
        policy.updatePolicy(player);
        int activePolicyCode = policy.getPolicy(); // 获取激活的策略代码
        
        // 给玩家发一张牌（使用策略）
        Card newCard = dealCard(game, activePolicyCode, true); // 传入 game, 策略代码, 玩家牌标识
        String y = game.getPlayerCards();//获取用户的牌
        String n = newCard.toString();//发的新牌
        game.setPlayerCards((y+","+n));//原来所有的牌加上新牌放进数据库中
        int a = calculateScore(Arrays.asList(newCard));//计算新牌的数值
        game.setPlayerScore(game.getPlayerScore() + a);//把新计算的数值放进数据库

        // 检查是否爆牌
        if (isBust(game.getPlayerScore())) {
            game.setGameStatus("庄家赢");
            game.setResult("玩家爆牌！庄家获胜！");
            fail(game.getPlayerId());
            game.setBetAmount(0);//筹码归零
            List<Card> cards = parseCards(game.getDealerCards());
            game.setDealerScore(calculateScore(cards));

            // 数据埋点：更新玩家数据
            if (player != null) { 
                player.setAllNumber(player.getAllNumber() + 1); // 增加总场次
                player.setConsecutiveLosses(player.getConsecutiveLosses() + 1); // 增加连输次数
                player.setConsecutiveWins(0); // 连赢次数清零
                player.setTotalLoss(player.getTotalLoss() + game.getBetAmount()); // 累加总亏损
                // 重新计算胜率
                if (player.getAllNumber() > 0) {
                    player.setWinning((int) ((double) player.getWinNumber() / player.getAllNumber() * 100));
                } else {
                    player.setWinning(0);
                }
                playerMapper.updateById(player); // 更新玩家记录
            }
        }

        return game;
    }
    // 玩家停牌
    @Override
    public Game stand(Integer gameId) {
        Game game = games.get(gameId);
        if (game == null) {
            throw new RuntimeException("游戏不存在");
        }

        if (!"进行中".equals(game.getGameStatus())) {
            throw new RuntimeException("游戏已结束");
        }

        // 显示庄家的完整手牌和点数
        List<Card> cards = parseCards(game.getDealerCards());
        game.setDealerScore(calculateScore(cards));

        // 获取玩家对象和发牌策略
        Player player = playerMapper.selectById(game.getPlayerId()); // 在方法开头声明一次
        Policy policy = new Policy();
        policy.updatePolicy(player);

        // 数据埋点：更新玩家数据（21点数据记录）
        if(game.getPlayerScore()==21){
            player.setWin21(player.getWin21()+1);
            playerMapper.updateById(player);
        }

        // 庄家要牌策略：点数小于17时必须要牌
        while (game.getDealerScore() < 17) {
            //获取新牌
            Card newCard = dealERCard(); // 使用 dealERCard()
            String y = game.getDealerCards();//获取庄家之前的牌
            String n = newCard.toString();//新发的牌
            game.setDealerCards((y+","+n));//所有的牌放进数据库
            int v = calculateScore(Arrays.asList(newCard));//计算新发牌的点数
            game.setDealerScore(game.getDealerScore() + v);//所有的点数和放进数据库

            // 检查庄家是否爆牌
            if (isBust(game.getDealerScore())) {
                game.setGameStatus("玩家赢");
                game.setResult("庄家爆牌！玩家获胜！");
                player.setWinNumber(player.getWinNumber()+1); 
                playerMapper.updateById(player);
                win(game.getPlayerId(), game.getPlayerScore());
                game.setBetAmount(game.getBetAmount()*2);//筹码翻倍
                return game;
            }
        }

        // 比较点数决定胜负
        boolean playerWon = false;
        if (game.getPlayerScore() > game.getDealerScore()) {
            game.setGameStatus("玩家赢");
            game.setResult("玩家点数更高！玩家获胜！");
            win(game.getPlayerId(), game.getPlayerScore());
            game.setBetAmount(game.getBetAmount()*2);//筹码翻倍
            playerWon = true;
        } else if (game.getPlayerScore() < game.getDealerScore()) {
            game.setGameStatus("庄家赢");
            game.setResult("庄家点数更高！庄家获胜！");
            fail(game.getPlayerId());
            game.setBetAmount(0);//筹码归零
            playerWon = false;
        } else {
            game.setGameStatus("平局");
            game.setResult("点数相同！平局！");
            // 数据埋点：更新玩家数据（平局）
            if (player != null) { 
                player.setAllNumber(player.getAllNumber() + 1); // 增加总场次
                // 重新计算胜率
                if (player.getAllNumber() > 0) {
                    player.setWinning((int) ((double) player.getWinNumber() / player.getAllNumber() * 100));
                } else {
                    player.setWinning(0);
                }
                playerMapper.updateById(player); // 更新玩家记录
            }
            return game;
        }
        // 数据埋点：更新玩家数据（赢或输）
        if (player != null) { 
            player.setAllNumber(player.getAllNumber() + 1); // 增加总场次
            if (playerWon) {
                player.setWinNumber(player.getWinNumber() + 1); // 增加胜场
                player.setConsecutiveWins(player.getConsecutiveWins() + 1); // 增加连赢次数
                player.setConsecutiveLosses(0); // 连输次数清零
            } else {
                player.setConsecutiveLosses(player.getConsecutiveLosses() + 1); // 增加连输次数
                player.setConsecutiveWins(0); // 连赢次数清零
                player.setTotalLoss(player.getTotalLoss() + game.getBetAmount()); // 累加总亏损
            }
            // 重新计算胜率
            if (player.getAllNumber() > 0) {
                player.setWinning((int) ((double) player.getWinNumber() / player.getAllNumber() * 100));
            } else {
                player.setWinning(0);
            }
            playerMapper.updateById(player); // 更新玩家记录
        }
        return game;
    }
    // 计算手牌点数
    @Override
    public int calculateScore(List<Card> cards) {
        int score = 0;
        int aces = 0;
        // 先计算所有牌的点数
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            if ("A".equals(card.getRank())) {
                score += 11;
                aces++; // 遇到 A 牌，累加 aces 计数
            } else {
                score += card.getValue();
            }
        }
        // 如果总分超过21且有A，则将A按1计算
        while (score > 21 && aces > 0) {
            score -= 10; // 将A从11改为1
            aces--;
        }
        return score;
    }
    //计算排位积分胜利加1
    public void win(int a,int b){//a为id号，b为点数
        Player player = playerMapper.selectById(a);
        player.setDanNumber(player.getDanNumber()+1);
        if(b==21){
            player.setDanNumber(player.getDanNumber()+5);
        }
        playerMapper.updateById(player);
        dan(a); // 更新段位
    }
    //计算排位积分失败减1
    public void fail(int a){
        Player player = playerMapper.selectById(a);
        if(player.getDanNumber()>0){
            player.setDanNumber(player.getDanNumber()-1);
        }
        playerMapper.updateById(player);
        dan(a); // 更新段位
    }
    //检查排位数据进行升级降级
    public void dan(int a){
        Player player = playerMapper.selectById(a);
        int danNumber = player.getDanNumber();
        String dan;
        if (danNumber >= 1501) {
            dan = "国际大师";
        } else if (danNumber >= 1401) {
            dan = "专家III";
        } else if (danNumber >= 1301) {
            dan = "专家II";
        } else if (danNumber >= 1201) {
            dan = "专家I";
        } else if (danNumber >= 1101) {
            dan = "高手III";
        } else if (danNumber >= 1001) {
            dan = "高手II";
        } else if (danNumber >= 901) {
            dan = "高手I";
        } else if (danNumber >= 801) {
            dan = "中级III";
        } else if (danNumber >= 701) {
            dan = "中级II";
        } else if (danNumber >= 601) {
            dan = "中级I";
        } else if (danNumber >= 501) {
            dan = "入门III";
        } else if (danNumber >= 401) {
            dan = "入门II";
        } else if (danNumber >= 301) {
            dan = "入门I";
        } else if (danNumber >= 201) {
            dan = "新手III";
        } else if (danNumber >= 101) {
            dan = "新手II";
        } else {
            dan = "新手I";
        }
        player.setDan(dan);
        playerMapper.updateById(player); // 更新玩家记录
    }

    // 检查是否爆牌
    @Override
    public boolean isBust(int score) {
        return score > 21;
    }
    // 检查是否黑杰克
    @Override
    public boolean isBlackJack(List<Card> cards) {
        return cards.size() == 2 && calculateScore(cards) == 21;
    }
    
    // 生成游戏ID
    private Integer generateGameId() {
        return random.nextInt(999998) + 1;
    }
} 