package cn.com.mryhl.controller;

import cn.com.mryhl.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class DemoController {
    // springmvc查找的时候是这样的:
    // 先找最标准的
    // 如果找不到,去掉后缀再次寻找
    // 方法处可以不写.do
    //@RequestMapping("/demoController/demo1.do")
    @RequestMapping("/demoController/demo1")
    public String demo1() {

        System.out.println("到达了后台demo1");
        // int i=1/0;
        // 方式一: 简单类型转发,这种方式是会拼接前后缀的
        return "success";
    }

    @RequestMapping("/demoController/demo2")
    public String demo2() {

        System.out.println("到达了后台demo2");
        // 方式二: forward关键字,这种方式不会拼接前后缀
        return "forward:/fail.jsp";
    }

    @RequestMapping("/demoController/demo3")
    public void demo3(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("到达了后台demo3");
        // 方式三: 原生api方式,这种方式不会拼接前后缀
        request.getRequestDispatcher("/WEB-INF/success.jsp").forward(request, response);
    }

    /**
     * 转发,传递参数
     *
     * @return
     */
    @RequestMapping("/demoController/demo111")
    public ModelAndView demo111(ModelAndView mv) {

        System.out.println("到达了后台demo1");
        // 添加数据
        mv.addObject("name", "龚戴朗");
        // 设置视图
        mv.setViewName("success");

        return mv;
    }
    /*
    // 方式二
    @RequestMapping("/demoController/demo111")
    public String demo111(Model model, ModelMap map) {

        System.out.println("到达了后台demo1");
        // 方式二: 使用Model进行传值,底层将数据放入request域进行数据的传递

        // 1. model方式
        // model.addAttribute("name","东方代分");
        // 2. map方式
        map.put("name","东方代分");
        return "success";
    }*/
    /*
    // 方式一
    @RequestMapping("/demoController/demo111")
    public String demo111(HttpServletRequest request) {

        System.out.println("到达了后台demo1");
        // 方式一: 使用request域进行参数传递
        request.setAttribute("name","公孙嘏");
        return "success";
    }*/


    /**
     * 重定向
     */
    @RequestMapping("/demoController/demo4")
    public String demo4() {
        System.out.println("到达了后台demo4");
        // 方式一:redirect方式重定向操作
        return "redirect:/fail.jsp";
    }

    /**
     * 方式二
     */
    @RequestMapping("/demoController/demo5")
    public void demo5(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("到达了后台demo5");
        // 方式二:使用原生的api
        response.sendRedirect(request.getContextPath() + "/fail.jsp");
    }

    /**
     * 通过重定向访问WEB-INF下资源(经过了一次转发)
     */
    @RequestMapping("/demoController/demo6")
    public String demo6() {
        System.out.println("到达了后台demo6");
        return "redirect:/demoController/demo1";
    }

    /**
     * @ResponseBody 用于将controller方法返回的对象通过转换器转换为指定的格式(通常为json)之后，再写回响应。
     * @RequestBody 在请求体中获取json类型的数据, 然后封装到指定集合或对象中
     */
    @ResponseBody
    @RequestMapping("/demoController/demo7")
    public List<User> demo7(@RequestBody List<User> users) {
        System.out.println("到达了后台demo7");
        System.out.println(users);
        return users;
    }


    /**
     * 查询
     */
    @ResponseBody
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public void findAll() {
        System.out.println("findAll");
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public void saveUser(User user) {
        System.out.println("saveUser");
    }

    /**
     * 查询一个
     */
    @ResponseBody
    @RequestMapping(value = "/user/id/{id}/name/{name}", method = RequestMethod.GET)
    public String findById(
            @PathVariable("id") Integer id,
            @PathVariable("name") String name
    ) {
        System.out.println(id + name);

        System.out.println("saveUser");
        return name;
    }

    /**
     * 拦截器模拟
     */
    @RequestMapping("/demoController/demo9")
    public String demo9() {

        System.out.println("到达了后台demo9,进入了controller.....");
        return "success";
        // 运行效果
        /*
        在controller之前执行=========1
        到达了后台demo9,进入了controller.....
        在离开controller之后执行=========1
        在页面渲染完之后执行=========1
         */
    }
}
