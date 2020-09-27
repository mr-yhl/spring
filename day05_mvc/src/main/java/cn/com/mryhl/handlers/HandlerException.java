package cn.com.mryhl.handlers;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 添加注解,需加入包的扫描
 */
@Component
public class HandlerException implements HandlerExceptionResolver {
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        // 记录日志
        System.out.println(new Date().toLocaleString()+e.getMessage());
        //返回页面
        ModelAndView mv = new ModelAndView();
        // 存在前后缀的拼接
        mv.setViewName("error");
        mv.addObject("message",e.getMessage());

        return mv;
    }
}
