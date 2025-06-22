package edu.czjtu.blackjack.mapper;

import edu.czjtu.blackjack.entity.Friend;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FriendMapper {

    @Insert("INSERT INTO friends(userId, friendId, remark, status) VALUES(#{userId}, #{friendId}, #{remark}, #{status})")
    void addFriend(Friend friend);

    @Delete("DELETE FROM friends WHERE userId = #{userId} AND friendId = #{friendId}")
    void deleteFriend(@Param("userId") int userId, @Param("friendId") int friendId);

    @Update("UPDATE friends SET remark = #{remark}, status = #{status} WHERE userId = #{userId} AND friendId = #{friendId}")
    void updateFriend(Friend friend);

    @Select("SELECT * FROM friends WHERE userId = #{userId}")
    List<Friend> getFriendsByUserId(@Param("userId") int userId);

    @Select("SELECT * FROM friends WHERE userId = #{userId} AND friendId = #{friendId}")
    Friend getFriendByIds(@Param("userId") int userId, @Param("friendId") int friendId);
} 