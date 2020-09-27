package cn.com.mryhl.handlers;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyHandlerInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String loginname = (String) request.getSession().getAttribute("loginname");

        if (loginname != null){
            return true;
        }else {
        // 返回登录页面
        response.sendRedirect(request.getContextPath()+"/login.jsp");

        return false;
        }
    }
}
