package edu.czjtu.blackjack.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;

// 玩家状态类 - 追踪玩家的各种游戏属性
@Data
@AllArgsConstructor//有参构造
@NoArgsConstructor//无参构造
@TableName("player")
public class Player {
    @TableId(type = IdType.ASSIGN_ID) // 使用ASSIGN_ID与User的ID类型保持一致
    private Integer playerId;  //用户id
    private double balance; // 当前余额
    private double initialBalance; // 初始余额
    private int consecutiveLosses; // 连输次数
    private int consecutiveWins; // 连赢次数
    private double totalLoss; // 总亏损
    private int winNumber; // 总胜场
    private int allNumber; // 总场次
    private int winning; //  胜率
    private int win21; //  21点次数
    private int allTime; //  游戏总时长
    private String dan; // 段位
    private int danNumber; // 段位积分
    private String danSort; // 段位世界排名

    public Player(Integer playerId, double initialBalance) { // 修改构造函数参数类型
        this.playerId = playerId;
        this.balance = initialBalance;
        this.initialBalance = initialBalance;
        this.consecutiveLosses = 0;
        this.consecutiveWins = 0;
        // this.recentBets = new ArrayList<>(); // 已删除
        this.totalLoss = 0;
    }

    // Getters and setters
    public Integer getPlayerId() { // 修改getter返回类型
        return playerId;
    }

    public void setPlayerId(Integer playerId) { // 修改setter参数类型
        this.playerId = playerId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getInitialBalance() {
        return initialBalance;
    }

    public int getConsecutiveLosses() {
        return consecutiveLosses;
    }

    public void setConsecutiveLosses(int consecutiveLosses) {
        this.consecutiveLosses = consecutiveLosses;
    }

    public int getConsecutiveWins() {
        return consecutiveWins;
    }

    public void setConsecutiveWins(int consecutiveWins) {
        this.consecutiveWins = consecutiveWins;
    }

    // public List<Double> getRecentBets() { return recentBets; } // 已删除

    public void addBet(double bet) {
        // recentBets.add(bet); // 已删除
        // if (recentBets.size() > 3) { recentBets.remove(0); } // 已删除
    }

    public double getTotalLoss() {
        return totalLoss;
    }

    public void updateTotalLoss(double amount) {
        this.totalLoss += amount;
    }

    // 游戏结果处理
    public void processWin(double bet) {
        consecutiveWins++;
        consecutiveLosses = 0;
        balance += bet * 2; // 赢回双倍下注
    }

    public void processLoss(double bet) {
        consecutiveWins = 0;
        consecutiveLosses++;
        balance -= bet;
        updateTotalLoss(bet);
    }
}