package edu.czjtu.blackjack.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.czjtu.blackjack.entity.Announcement;
import edu.czjtu.blackjack.mapper.AnnouncementMapper;
import edu.czjtu.blackjack.service.AnnouncementService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公告服务实现类
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    @Resource
    private AnnouncementMapper announcementMapper;

    @Override
    public List<Announcement> getAllAnnouncements() {
        return announcementMapper.selectList(null); // 查询所有公告
    }
} 