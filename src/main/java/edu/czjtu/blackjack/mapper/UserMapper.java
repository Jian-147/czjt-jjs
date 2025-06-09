package edu.czjtu.blackjack.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.czjtu.blackjack.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
//BaseMapper接口中已经写好了现成的增删改查方法，只需要继承就可以使用

}
