package com.company.wenda.controller;

import com.company.wenda.aspect.LogAspect;
import com.company.wenda.model.User;
import com.company.wenda.service.WendaService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.util.*;

@Controller//入口层
public class IndexController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired              //依赖注入
    WendaService wendaService;
    //WendaService wendaService = new WendaService();

    @RequestMapping(path = {"/", "/index"},  method = {RequestMethod.GET})//url，访问地址
    //写入数据时一般用POST，获取数据时用GET，通过method进行设置
    //PUT支持幂等性
    @ResponseBody//直接返回字符串而不是模板
    public String index(HttpSession httpSession){
        logger.info("VISIT HOME");
        return wendaService.getMessage(1) + "Hello NowCoder" + httpSession.getAttribute("msg");
    }

    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") String groupId,
                          @RequestParam(value = "type", defaultValue = "1") int type,//默认值可以用在首页
                          @RequestParam(value = "key", defaultValue = "zz", required = false) String key){
        return String.format("Profile Page of %s / %d, t: %d k: %s", groupId, userId, type, key);
    }
    //例：http://127.0.0.1:8080/profile/miao/324?type=2&key=z


    @RequestMapping(path = {"/ftl"},  method = {RequestMethod.GET})
    //不直接渲染，通过模板渲染
    public String template(Model model){
        model.addAttribute("value1", "vvvvv1");
        List<String> colors = Arrays.asList(new String[]{"RED","GREEN","BLUE"});
        model.addAttribute("colors", colors);

        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 4; ++i){
            map.put(String.valueOf(i), String.valueOf(i*i));
        }
        model.addAttribute("map", map);
        model.addAttribute("user", new User("LEE"));
        return "home";
    }

    @RequestMapping(path = {"/request"},  method = {RequestMethod.GET})
    @ResponseBody
    public String request(Model model, HttpServletResponse response,
                          HttpServletRequest request,
                          HttpSession httpSession,
                          @CookieValue("JSESSIONID") String sessionId){
        StringBuilder sb = new StringBuilder();
        sb.append("COOKIEVALUE:" + sessionId);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }
        if (request.getCookies() != null){
            for (Cookie cookie : request.getCookies()){
                sb.append("Cookie:" + cookie.getName() + " value:" + cookie.getValue());
            }
        }
        sb.append(request.getMethod() + "<br>");
        sb.append(request.getQueryString() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURI() + "<br>");
        sb.append(request.getRequestURL() + "<br>");

        response.addHeader("nowcoderId","hello");
        response.addCookie(new Cookie("username","nowcoder"));
        //response.getOutputStream().write();写入二进制的流，可以将图片的流读取出来，例如验证码。
        return sb.toString();
        //例:http://127.0.0.1:8080/request?type=2
    }

    @RequestMapping(path = {"/redirect/{code}"},  method = {RequestMethod.GET})
    public RedirectView redirect(@PathVariable("code") int code,
                           HttpSession httpSession){
        httpSession.setAttribute("msg","jump from redirect");
        RedirectView red = new RedirectView("/", true);
        if (code == 301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;
        //return "redirect:/";   //此处函数返回类型为String
    }
    //http://127.0.0.1:8080/redirect/301
    //http://127.0.0.1:8080/redirect/302

    @RequestMapping(path = {"/admin"},  method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key") String key){
        if ("admin".equals(key)){
            return "hello admin";
        }
        throw new IllegalArgumentException("参数不对");
    }
    //http://127.0.0.1:8080/admin?key=admin
    //http://127.0.0.1:8080/admin?key=admin2


    @ExceptionHandler()
    //ExceptionHandler，自定义捕获异常（系统出现spring没处理的异常时，跳到这里）
    //可以指定统一的异常处理（如统一的异常页面）
    @ResponseBody
    public String error(Exception e){
        return "error:" + e.getMessage();
    }

}
