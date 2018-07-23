package com.company.wenda.controller;

import com.company.wenda.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class SettingController {
    @Autowired              //依赖注入，不需要初始化
    WendaService wendaService;
    //WendaService wendaService = new WendaService();

    @RequestMapping(path = {"/setting"},  method = {RequestMethod.GET})
    @ResponseBody
    public String setting(){
        return "Setting OK. " + wendaService.getMessage(1);
    }
}
