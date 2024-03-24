package top.rabbitbyte.nowblog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.rabbitbyte.nowblog.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-03-08 15:42:11
* @Entity top.rabbitbyte.nowblog.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    User selectByName(@Param("username") String username);

    User selectByEmail(@Param("email") String email);

    void updateStatus(@Param("userid")int userId,@Param("status") int status);

    int updateHeader(@Param("userid") int userId,@Param("headerUrl") String headerUrl);

    void updatePassword(@Param("user") User u);
}




