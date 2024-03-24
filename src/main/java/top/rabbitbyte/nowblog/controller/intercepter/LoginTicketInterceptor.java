package top.rabbitbyte.nowblog.controller.intercepter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.rabbitbyte.nowblog.entity.LoginTicket;
import top.rabbitbyte.nowblog.entity.User;
import top.rabbitbyte.nowblog.service.impl.UserServiceImpl;
import top.rabbitbyte.nowblog.util.CookieUtil;
import top.rabbitbyte.nowblog.util.HostHolder;

import java.util.Date;

@Component
public class LoginTicketInterceptor  implements HandlerInterceptor {
    @Autowired
    private UserServiceImpl userService;
    // 登录拦截器
    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从cookie获取登录凭证

        String  ticket = CookieUtil.getValue(request, "ticket");
        LoginTicket loginTicket  =  userService.findLoginTicket(ticket);

        // 判断用户是否登录
        // 判断用户是否登录成功
        // 判断用户是否登录超时
        if (loginTicket != null && loginTicket.getExpired().after(new Date()) && loginTicket.getStatus() == 0 ){
            //根据凭据查找用户
            User user =  userService.findUserById(String.valueOf(loginTicket.getUserId()));
            // 在本次请求中持有用户
            hostHolder.setUser(user);
        }
        return true;
    }

    /**
     * 模板渲染之前，controller处理完之后
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        User user  = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
