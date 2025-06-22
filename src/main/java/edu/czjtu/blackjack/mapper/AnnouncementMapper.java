package edu.czjtu.blackjack.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.czjtu.blackjack.entity.Announcement;
import org.apache.ibatis.annotations.Mapper;

/**
 * 公告 Mapper 接口
 */
@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {
} 