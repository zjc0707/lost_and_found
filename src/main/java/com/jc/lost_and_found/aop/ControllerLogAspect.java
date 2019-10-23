package com.jc.lost_and_found.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 对所有HTTP请求进行截获输出相应参数，程序调试用
 * @author  sample
 * @date    2019/06/30
 */
@Slf4j
@Aspect
@Component
public class ControllerLogAspect {

    @Pointcut("execution(public * com.jc.lost_and_found.controller..*(..))")
    public void log(){}

    @Before("log()")
    public void beforeAllControllerLog(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        log.info("*******************Start Request*******************************");
        log.info("Request URL : " + request.getRequestURL().toString());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("SourceIP : " + request.getRemoteAddr());
        log.info("SessionID : " + request.getSession().getId());
        Enumeration<String> enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String name = enu.nextElement();
            log.info("KeyName:{},Value:{}", name, request.getParameter(name));
        }
        log.info("*******************End Request*********************************");
    }

}
