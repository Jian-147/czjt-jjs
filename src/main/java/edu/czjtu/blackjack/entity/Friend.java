package edu.czjtu.blackjack.entity;

public class Friend {
    private int userId;
    private int friendId;
    private String remark;
    private String status;

    public Friend() {
    }

    public Friend(int userId, int friendId, String remark, String status) {
        this.userId = userId;
        this.friendId = friendId;
        this.remark = remark;
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 