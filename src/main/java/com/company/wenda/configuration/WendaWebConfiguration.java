package com.company.wenda.configuration;

import com.company.wenda.interceptor.LoginRequredInterceptor;
import com.company.wenda.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
//Spring在初始化时会将拦截器加到请求链路上
public class WendaWebConfiguration extends WebMvcConfigurerAdapter {
    //Spring 5.0 WebMvcConfigurerAdapter已被废弃,待替换
    //用WebMvcConfigurationSupport出现CSS无法加载问题
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequredInterceptor loginRequredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);      //将拦截器加到请求链路上
        registry.addInterceptor(loginRequredInterceptor).addPathPatterns("/user/*");    //注意顺序，第二个拦截器用了第一个的变量
        super.addInterceptors(registry);
    }
}
