package edu.czjtu.blackjack.service;

import edu.czjtu.blackjack.entity.Friend;
import edu.czjtu.blackjack.mapper.FriendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendService {

    @Autowired
    private FriendMapper friendMapper;

    public void addFriend(Friend friend) {
        friendMapper.addFriend(friend);
    }

    public void deleteFriend(int userId, int friendId) {
        friendMapper.deleteFriend(userId, friendId);
    }

    public void updateFriend(Friend friend) {
        friendMapper.updateFriend(friend);
    }

    public List<Friend> getFriendsByUserId(int userId) {
        return friendMapper.getFriendsByUserId(userId);
    }

    public Friend getFriendByIds(int userId, int friendId) {
        return friendMapper.getFriendByIds(userId, friendId);
    }
} 