package com.company.wenda.service;

import com.company.wenda.controller.HomeController;
import com.company.wenda.dao.LoginTicketDAO;
import com.company.wenda.dao.UserDAO;
import com.company.wenda.model.HostHolder;
import com.company.wenda.model.LoginTicket;
import com.company.wenda.model.User;
import com.company.wenda.util.WendaUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
import org.apache.commons.lang.StringUtils;
import sun.security.provider.MD5;

import java.util.*;

@Service
public class UserService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    UserDAO userDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    //返回用了Map，因为返回会有各种各样的属性（已被注册啊balabala，要写在返回里。注册成功一般返回空）
    public Map<String, String> register(String username, String password){
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isBlank(username)){
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if (user != null){
            map.put("msg", "用户名已被注册");
            return map;
        }

        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));//生成随机字符串，截取五个字符
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",
                new Random().nextInt(1000)));
        user.setPassword(WendaUtil.MD5(password+user.getSalt()));//把原密码跟salt连起来后，通过MD5加密
        userDAO.addUser(user);

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);

        return map;
    }

    public Map<String, String> login(String username, String password){
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isBlank(username)){
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if (user == null){
            map.put("msg", "用户名不存在");
            return map;
        }

        if (!WendaUtil.MD5(password+user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "密码错误");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    public String addLoginTicket (int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date now = new Date();
        now.setTime(now.getTime() + 3600*24*1000);
        loginTicket.setExpired(now);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));//UUID中间有“-”，将其替换掉
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }

    public User getUser(int id){
        return userDAO.selectByid(id);
    }

}
