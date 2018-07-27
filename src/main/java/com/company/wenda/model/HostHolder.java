package com.company.wenda.model;

import org.springframework.stereotype.Component;

//存放通过ticket取出的用户，以便后面的链路访问
@Component  //依赖注入
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<User>();    //线程局部变量（应对多条线程同时访问）

    //根据当前线程找到相应的变量
    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
