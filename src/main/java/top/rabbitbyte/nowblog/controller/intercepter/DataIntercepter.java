package top.rabbitbyte.nowblog.controller.intercepter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import top.rabbitbyte.nowblog.entity.User;
import top.rabbitbyte.nowblog.service.DataService;
import top.rabbitbyte.nowblog.util.HostHolder;

import javax.xml.crypto.Data;

@Component
public class DataIntercepter implements HandlerInterceptor {
    @Autowired
    private DataService  dataService;
    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //统计uv
        String ip =  request.getRemoteHost();
        dataService.recordUV(ip);
        //统计dau
        User user = hostHolder.getUser();
        if (user != null){
            dataService.recordDAU(user.getId());
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
