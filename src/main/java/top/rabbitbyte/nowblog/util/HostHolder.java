package top.rabbitbyte.nowblog.util;

import org.springframework.stereotype.Component;
import top.rabbitbyte.nowblog.entity.User;

/**
 * 暂存一下用户信息，
 * 用于代替session
 */
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser(){
        return  users.get();
    }
    public void setUser(User user){
        users.set(user);
    }
    public  void clear(){
        users.remove();
    }
}
