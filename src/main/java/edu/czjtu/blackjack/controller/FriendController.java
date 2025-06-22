package edu.czjtu.blackjack.controller;

import edu.czjtu.blackjack.entity.Friend;
import edu.czjtu.blackjack.service.FriendService;
import edu.czjtu.blackjack.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("/add")
    public ResponseEntity<Result> addFriend(@RequestBody Friend friend) {
        try {
            friendService.addFriend(friend);
            return ResponseEntity.ok(Result.success());
        } catch (Exception e) {
            return ResponseEntity.ok(Result.error("500", "添加好友失败: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{userId}/{friendId}")
    public ResponseEntity<Result> deleteFriend(@PathVariable int userId, @PathVariable int friendId) {
        try {
            friendService.deleteFriend(userId, friendId);
            return ResponseEntity.ok(Result.success());
        } catch (Exception e) {
            return ResponseEntity.ok(Result.error("500", "删除好友失败: " + e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Result> updateFriend(@RequestBody Friend friend) {
        try {
            friendService.updateFriend(friend);
            return ResponseEntity.ok(Result.success());
        } catch (Exception e) {
            return ResponseEntity.ok(Result.error("500", "更新好友失败: " + e.getMessage()));
        }
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<Result> getFriendsByUserId(@PathVariable int userId) {
        try {
            List<Friend> friends = friendService.getFriendsByUserId(userId);
            return ResponseEntity.ok(Result.success(friends));
        } catch (Exception e) {
            return ResponseEntity.ok(Result.error("500", "获取好友列表失败: " + e.getMessage()));
        }
    }

    @GetMapping("/{userId}/{friendId}")
    public ResponseEntity<Result> getFriendByIds(@PathVariable int userId, @PathVariable int friendId) {
        try {
            Friend friend = friendService.getFriendByIds(userId, friendId);
            if (friend != null) {
                return ResponseEntity.ok(Result.success(friend));
            } else {
                return ResponseEntity.ok(Result.error("404", "未找到好友"));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(Result.error("500", "获取好友信息失败: " + e.getMessage()));
        }
    }
} 