package edu.czjtu.blackjack.common;

import org.springframework.stereotype.Component;
import java.util.Random;

//随机生成id
@Component
public class IdGenerator {
    private static final Random random = new Random();
    
    public static Integer generateUserId() {
        // 生成1-99999999之间的随机数
        return random.nextInt(99999999) + 1;
    }
    
    public static String formatUserId(Integer id) {
        // 将数字格式化为8位，不足补0
        return String.format("%08d", id);
    }
} 