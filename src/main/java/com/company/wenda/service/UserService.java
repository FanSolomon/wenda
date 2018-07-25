package com.company.wenda.service;

import com.company.wenda.dao.UserDAO;
import com.company.wenda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    public User getUser(int id){
        return userDAO.selectByid(id);
    }

}
