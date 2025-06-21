package edu.czjtu.blackjack.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.czjtu.blackjack.entity.Player;
import edu.czjtu.blackjack.mapper.PlayerMapper;
import edu.czjtu.blackjack.service.PlayerService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl extends ServiceImpl<PlayerMapper, Player> implements PlayerService {

    @Resource
    private PlayerMapper playerMapper;

    @Override
    public Player getPlayerById(Integer playerId) {
        return playerMapper.selectById(playerId);
    }

    @Override
    public void updatePlayerAllTime(Integer playerId, Integer addedTime) {
        Player player = playerMapper.selectById(playerId);
        if (player != null) {
            // 确保 allTime 不为 null，初始为0
            // 由于 allTime 是 int 类型，默认值为 0，无需检查 null
            player.setAllTime(player.getAllTime() + addedTime);
            playerMapper.updateById(player);
        } else {
            throw new RuntimeException("玩家不存在，无法更新在线时长");
        }
    }
} 