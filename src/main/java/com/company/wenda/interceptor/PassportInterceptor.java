package com.company.wenda.interceptor;


import com.company.wenda.dao.LoginTicketDAO;
import com.company.wenda.dao.UserDAO;
import com.company.wenda.model.HostHolder;
import com.company.wenda.model.LoginTicket;
import com.company.wenda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.handler.Handler;
import java.util.Date;

//第一个拦截器
//一个拦截器一般只完成一件事
@Component
public class PassportInterceptor implements HandlerInterceptor{
    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    HostHolder hostHolder;

    //请求开始之前调用
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        String ticket = null;
        if (httpServletRequest.getCookies() != null){
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

        if (ticket != null) {
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() !=0) {
                return true;
            }

            User user = userDAO.selectByid(loginTicket.getUserId());
            hostHolder.setUser(user);       //将用户信息放到ThreadLocal之中
        }

        return true;
        //返回false时整个请求结束，即被拦截，这样就不会继续执行下去了
    }

    //Handle处理完之后调用
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            //modelAndView,在模板（渲染的页面）中可以直接访问变量
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    //所有渲染完之后调用
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();     //清除变量
    }
}
