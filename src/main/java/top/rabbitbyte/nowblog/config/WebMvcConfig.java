package top.rabbitbyte.nowblog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.rabbitbyte.nowblog.controller.intercepter.DataIntercepter;
import top.rabbitbyte.nowblog.controller.intercepter.LoginRequiredInterceptor;
import top.rabbitbyte.nowblog.controller.intercepter.LoginTicketInterceptor;
import top.rabbitbyte.nowblog.controller.intercepter.MessageInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Autowired
    private DataIntercepter dataIntercepter;
    // 添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginTicketInterceptor).excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");
        //.addPathPatterns("/register","/login")
        registry.addInterceptor(loginRequiredInterceptor).excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");
        //.addPathPatterns("/register","/login")

        registry.addInterceptor(dataIntercepter).excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

    }
}
