package edu.czjtu.blackjack.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data//get，set方法
@AllArgsConstructor//有参构造
@NoArgsConstructor//无参构造
@TableName("user")
public class User {

    @TableId
    private Integer id;//主键ID
    private String username;//用户名
    private String password;//密码
    private String email;//邮箱
    private String phone;//手机号
    private String userPic;//用户头像地址
    private Integer balance;//用户余额
    private String state;//用户状态
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;//创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;//更新时间

}
