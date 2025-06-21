package edu.czjtu.blackjack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.czjtu.blackjack.entity.Player;

public interface PlayerService extends IService<Player> {
    // 根据玩家ID获取玩家数据
    Player getPlayerById(Integer playerId);

    // 更新玩家总在线时长
    void updatePlayerAllTime(Integer playerId, Integer addedTime);
} 
 