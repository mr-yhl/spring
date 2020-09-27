package cn.com.mryhl.handlers;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 此注解标注一个类上,代表是一个全局异常处理器的类
 */
@ControllerAdvice
public class HandlerException2{
    /**
     * 标在方法上,支持一个value属性,定义当前方法可以处理异常类型
     */
    @ExceptionHandler(Exception.class)
    public String resolveException2(Exception e,HttpServletRequest httpServletRequest) {
        // 记录日志
        System.out.println(new Date().toLocaleString()+e.getMessage());
        // 返回页面
        httpServletRequest.setAttribute("message", e.getMessage());
        return "error";
    }
}
