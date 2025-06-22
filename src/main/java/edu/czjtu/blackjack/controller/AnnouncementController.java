package edu.czjtu.blackjack.controller;

import edu.czjtu.blackjack.common.Result;
import edu.czjtu.blackjack.entity.Announcement;
import edu.czjtu.blackjack.service.AnnouncementService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 公告控制器
 */
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    @Resource
    private AnnouncementService announcementService;

    /**
     * 获取所有公告
     * @return 结果对象，包含公告列表
     */
    @GetMapping("/all")
    public Result getAllAnnouncements() {
        List<Announcement> announcements = announcementService.getAllAnnouncements();
        return Result.success(announcements);
    }

    /**
     * 添加公告
     */
    @PostMapping("/add")
    public Result addAnnouncement(@RequestBody Announcement announcement) {
        announcement.setPublishDate(LocalDateTime.now());
        boolean saved = announcementService.save(announcement);
        if (saved) {
            return Result.success(announcement);
        } else {
            return Result.error("公告添加失败");
        }
    }

    /**
     * 修改公告
     */
    @PostMapping("/update")
    public Result updateAnnouncement(@RequestBody Announcement announcement) {
        boolean updated = announcementService.updateById(announcement);
        if (updated) {
            return Result.success(announcement);
        } else {
            return Result.error("公告修改失败");
        }
    }

    /**
     * 删除公告
     */
    @PostMapping("/delete")
    public Result deleteAnnouncement(@RequestBody Announcement announcement) {
        boolean removed = announcementService.removeById(announcement.getId());
        if (removed) {
            return Result.success("公告删除成功");
        } else {
            return Result.error("公告删除失败");
        }
    }
} 