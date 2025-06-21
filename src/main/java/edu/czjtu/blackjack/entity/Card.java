package edu.czjtu.blackjack.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    private String suit;    // 用来储存花色：♠♥♣♦
    private String rank;    // 用来储存点数：A,2,3,4,5,6,7,8,9,10,J,Q,K
    private int value;      // 21点中的数值
    
    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
        this.value = calculateValue(rank);
    }
    
    private int calculateValue(String rank) {
        switch (rank) {
            case "A": return 11;  // A可以算1或11，这里先算11
            case "K":
            case "Q":
            case "J": return 10;
            default: return Integer.parseInt(rank);
        }
    }
    
    @Override
    public String toString() {
        return suit + rank;
    }   //输出花色和点数
} 