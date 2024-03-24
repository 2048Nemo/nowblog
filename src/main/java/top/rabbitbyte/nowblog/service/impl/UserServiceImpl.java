package top.rabbitbyte.nowblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import top.rabbitbyte.nowblog.entity.LoginTicket;
import top.rabbitbyte.nowblog.entity.User;
import top.rabbitbyte.nowblog.service.UserService;
import top.rabbitbyte.nowblog.mapper.UserMapper;
import org.springframework.stereotype.Service;
import top.rabbitbyte.nowblog.util.CommunityUtil;
import top.rabbitbyte.nowblog.util.MailClient;
import top.rabbitbyte.nowblog.util.RedisKeyUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static top.rabbitbyte.nowblog.util.CommunityConstant.*;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-03-08 15:42:11
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;
//
//    @Autowired
//    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath ;

    @Override
    public User findUserById(String id) {
        //        return userMapper.selectById(id);
        User user = getCache(Integer.parseInt(id));
        if (user == null) {
            user = initCache(Integer.parseInt(id));
        }
        return user;
    }

    @Override
    public int updateHeader(Integer id, String headerUrl) {
        int rows = userMapper.updateHeader(id,headerUrl);
        clearCache(id);
        return rows;
    }


    @Override
    public Map<String, Object> updatePassword(User user, String oldPassword, String newPassword,String confirmPassword) {
        Map<String ,Object> map =  new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(oldPassword)){

            map.put("oldPasswordMsg","旧密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(newPassword)){
            map.put("newPasswordMsg","新密码不能为空");
            return map;
        }

        if (StringUtils.isBlank(confirmPassword)){
            map.put("confirmPasswordMsg","重复输入密码不能为空");
            return map;
        }
        if (!confirmPassword.equals(newPassword)){
            map.put("confirmPasswordMsg","重复密码输入错误");
            return map;
        }


        // 验证账号
        User u = userMapper.selectByName(user.getUsername());

        // 验证密码
         oldPassword = CommunityUtil.generateMD5(oldPassword + u.getSalt());

        assert oldPassword != null;

        if (oldPassword.equals(u.getPassword())) {
            //旧密码验证成功需要修改密码
            String newSalt =  CommunityUtil.generateUUID().substring(0,5);
            u.setPassword(CommunityUtil.generateMD5(newPassword + newSalt));
            u.setSalt(newSalt);
            userMapper.updatePassword(u);
            map.put("success","");
        }else {
            map.put("oldPasswordMsg","旧密码输入错误");
            return map;
        }

        return map;
    }

    @Override
    public User findUserByName(String toName) {
        return userMapper.selectByName(toName);
    }

    public Map<String ,Object> register(User user){
        Map<String ,Object> map =  new HashMap<>();

        // 空值处理
        if (user== null){
            throw new IllegalArgumentException("参数不能为空");
        }
        if (StringUtils.isBlank(user.getUsername())){

            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }

        // 验证账号
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "该账号已存在");
            return map;
        }

        // 验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "该邮箱已被注册");
            return map;
        }
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.generateMD5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insert(user);
        //激活邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail()) ;

        String url =  domain + contextPath+"/activation/"+user.getId()+"/"+user.getActivationCode();
        context.setVariable("url",url);
        mailClient.sendMail(user.getEmail(),"激活账号",templateEngine.process("/mail/activation",context));
        return map;
    }

    public int activate(int userId,String code){
        User user = userMapper.selectById(userId);
        if (user.getStatus()== 1 ){
            return ACTIVATION_REPEAT;
        }else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId,1);
            clearCache(userId);
            return ACTIVATION_SUCCESS;
        }else {
            return ACTIVATION_FAILURE;
        }
    }


    public Map<String ,Object> login(String username,String password,int expiredSeconds){
        Map<String ,Object> map =  new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)){

            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空");
            return map;
        }

        // 验证账号
        User u = userMapper.selectByName(username);
        if (u == null) {
            map.put("usernameMsg", "该账号不存在");
            return map;
        }
        // 验证状态
        if (u.getStatus()== 0) {
            map.put("usernameMsg", "该账号未激活");
            return map;
        }

        // 验证密码
        password = CommunityUtil.generateMD5(password+u.getSalt());

        if (password.equals(u.getPassword())) {
             //登录成功
             LoginTicket loginTicket = new LoginTicket();
             loginTicket.setUserId(u.getId());
             loginTicket.setTicket(CommunityUtil.generateUUID());
             loginTicket.setStatus(0);
             loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds));
//             loginTicketMapper.insertLoginTicket(loginTicket);
            String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
            redisTemplate.opsForValue().set(redisKey,loginTicket);
             map.put("ticket",loginTicket.getTicket());
        }

        return map;
    }
    public void logout(String ticket){
//        loginTicketMapper.updateStatus(ticket, 1);
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey, loginTicket);
    }

    public LoginTicket findLoginTicket(String ticket){
//        return loginTicketMapper.selectByTicket(ticket);
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }


    // 1.优先从缓存中取值
    private User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    // 2.取不到时初始化缓存数据
    private User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }

    // 3.数据变更时清除缓存数据
    private void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }

}



