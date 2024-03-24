package top.rabbitbyte.nowblog.service;

import top.rabbitbyte.nowblog.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Service
* @createDate 2024-03-08 15:42:11
*/
public interface UserService extends IService<User> {

    User findUserById(String userId);

    int updateHeader(Integer id, String headerUrl);

    Map<String, Object> updatePassword(User user, String oldPassword, String newPassword, String confirmPassword);

    User findUserByName(String toName);
}
