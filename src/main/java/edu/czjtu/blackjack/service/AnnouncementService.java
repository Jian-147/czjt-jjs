package edu.czjtu.blackjack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.czjtu.blackjack.entity.Announcement;

import java.util.List;

/**
 * 公告服务接口
 */
public interface AnnouncementService extends IService<Announcement> {
    /**
     * 获取所有公告
     * @return 所有公告的列表
     */
    List<Announcement> getAllAnnouncements();
} 