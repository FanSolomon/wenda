package com.company.wenda.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.logging.Logger;

@Aspect             //面向切面(可用于了解网站性能)
@Component          //以组件的方式在依赖注入时把这个对象构造出来

//在所有调用IndexController和SettingController方法之前（之后）都调用beforeMethod（afterMethod）方法
public class LogAspect {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LogAspect.class);
    //private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.company.wenda.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder();
        for (Object arg : joinPoint.getArgs()){
            sb.append("args:" + arg.toString() + "|");
        }
        logger.info("before method" + new Date() + sb.toString());
    }

    @After("execution(* com.company.wenda.controller.*Controller.*(..))")
    public void afterMethod(){
        logger.info("after method" + new Date());
    }
}
