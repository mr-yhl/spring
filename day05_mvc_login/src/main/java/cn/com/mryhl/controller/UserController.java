package cn.com.mryhl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    /**
     * 登录
     * @return
     */
    @RequestMapping("/userController/login")
    public String login(String username, String password, HttpSession session) {
        if ("admin".equals(username)){
            // 记录session
            session.setAttribute("loginname",username);

            // 跳转到主页面
            return "redirect:/userController/toIndex";
        }else {
            //登录失败
            return "redirect:/login.jsp";
        }

    }


    /**
     * 跳转到主页
     * @return
     */
    @RequestMapping("/userController/toIndex")
    public String toIndex(){
        return "index";
    }
}
