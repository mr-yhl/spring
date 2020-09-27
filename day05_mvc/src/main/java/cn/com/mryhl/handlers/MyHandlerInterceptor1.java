package cn.com.mryhl.handlers;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器
 * 实现HandlerInterceptor接口，重写方法
 */
public class MyHandlerInterceptor1 implements HandlerInterceptor {
    /**
     *  在controller之前执行
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("在controller之前执行=========1");
        // 此返回值代表的是 是否放行当前请求 默认值是false(禁止通行) true(放行)
        return true;
    }

    /**
     * 在离开controller之后执行
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("在离开controller之后执行=========1");
    }

    /**
     * 页面渲染完毕后执行
     */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("在页面渲染完之后执行=========1");
    }
}
