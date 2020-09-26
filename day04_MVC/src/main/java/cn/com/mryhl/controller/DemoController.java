package cn.com.mryhl.controller;

import cn.com.mryhl.domain.User;
import cn.com.mryhl.domain.Vo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Controller将当前类放入SpringMVC容器中
 */
@Controller
@RequestMapping("/demoController")
public class DemoController {
    /**
     * 编写控制器
     *
     * @RequestMapping:用于为当前方法绑定一个URL地址,作用就是为了前端请求的匹配
     */
    @RequestMapping("/demo1")
    public String demo01() {
        System.out.println("后台执行了...");
        // 返回值就是下一步要转发的页面
        return "success";
    }

    /**
     * 编写控制器
     *
     * @RequestMapping:用于为当前方法绑定一个URL地址,作用就是为了前端请求的匹配
     */
    @RequestMapping(path = {"/demo2", "/demo3"})
    public String demo02() {
        System.out.println("后台执行了...");
        // 返回值就是下一步要转发的页面
        return "success";
    }

    /**
     * 编写控制器
     *
     * @RequestMapping:用于为当前方法绑定一个URL地址,作用就是为了前端请求的匹配 method, 作用就是用于限定当前方法的提交方式, 支持数组写法(同时多个)
     * 如果不写method,代表所有请求方式都能运行
     */
    @RequestMapping(value = "/demo4", method = RequestMethod.POST)
    public String demo04() {
        System.out.println("value = \"/demo3\",method = RequestMethod.POST");
        // 返回值就是下一步要转发的页面
        return "success";
    }

    /**
     * 编写控制器
     *
     * @RequestMapping:用于为当前方法绑定一个URL地址,作用就是为了前端请求的匹配 params用于限定请求参数的必传，不写代表不限制
     */
    @RequestMapping(value = "/demo5", params = "username")
    public String demo05() {
        System.out.println("username");
        // 返回值就是下一步要转发的页面
        return "success";
    }

    /**
     * 编写控制器,接收简单类型
     * 简单类型参数接收:需要保证前端传递的参数名称跟方法的形参名称一致就好
     * 对于简单类型的数据,SpringMVC底层内置了类型转换器
     */
    @RequestMapping(value = "/demo6")
    public String demo06(String username, Integer age) {
        System.out.println("username = " + username + ", age = " + age);
        // 返回值就是下一步要转发的页面
        return "success";
    }

    /**
     * 编写控制器,接收对象类型
     * 接收对象类型：需要保证前端传递的参数名称跟pojo的属性名称（set方法）一致就好
     */
    @RequestMapping(value = "/demo7")
    public String demo07(User user) {
        System.out.println(user);
        // 返回值就是下一步要转发的页面
        return "success";
    }

    /**
     * 如果前端传入的是数组,但是后台以字符串接收, Spring会将参数以,分隔拼接成一个串传进来[ public String demo8(String students)]
     * 如果前端传入的是数组,后台也是以数组接收, 只需要保证前端传递的参数名称跟方法中的数组形参名称一致就好
     */
    @RequestMapping(value = "/demo8")
    public String demo08(String[] students) {
        for (String student : students) {
            System.out.println(student);
        }
        // 返回值就是下一步要转发的页面
        return "success";
    }

    /**
     * 获取集合参数时，要将集合参数包装到一个pojo中才可以
     */
    @RequestMapping(value = "/demo9")
    public String demo9(Vo vo) {
        System.out.println(vo);
        // 返回值就是下一步要转发的页面
        return "success";
    }

    /**
     * 获取日期类型参数
     */
    @RequestMapping(value = "/demo10")
    public String demo10(Date myDate) {
        System.out.println(myDate);
        // 返回值就是下一步要转发的页面
        return "success";
    }

    /**
     * 文件上传
     * 参数类型必须是MultipartFile,参数名必须域前台一致
     */
    @RequestMapping(value = "/demo11")
    public String demo11(MultipartFile uploadFile) throws IOException {
        // 生产新的文件名
        String newFileName = UUID.randomUUID().toString() + uploadFile.getOriginalFilename();
        // 定义一个本地目录
        File newFile = new File(new File("F:\\code\\spring\\day04_MVC\\file"), newFileName);
        System.out.println(newFile);
        uploadFile.transferTo(newFile);

        return "success";
    }

    /**
     * 文件上传
     * 参数类型必须是MultipartFile,参数名必须域前台一致
     */
    @RequestMapping(value = "/demo12")
    public String demo12(MultipartFile[] uploadFiles) throws IOException {
        for (MultipartFile uploadFile : uploadFiles) {
            // 生产新的文件名
            String newFileName = UUID.randomUUID().toString() + uploadFile.getOriginalFilename();
            // 定义一个本地目录
            File newFile = new File(new File("F:\\code\\spring\\day04_MVC\\file"), newFileName);
            System.out.println(newFile);

            uploadFile.transferTo(newFile);
        }

        return "success";
    }

    /**
     * @RequestParam 标注在方法参数之前, 用于表示当前参数是获取的前端传递过来的哪个参数的值
     * @RequestParam 标注在方法参数之前, 此参数就是必传选项,不传就会报错 , 但是可以使用required=false来取消这个限制
     * defaultValue  可以为当前参数设置一个默认值, 当前端不再传递此参数的时候,就使用默认值
     * @RequestParam(value = "students")  可以接收一个集合参数,直接封装到一个集合对象中去
     */
    @RequestMapping(value = "/demo13")
    public String demo13(
            @RequestParam(value = "page_size", required = false, defaultValue = "20") String pageSize,
            @RequestParam(value = "students") List<String> students
    ) {
        System.out.println(pageSize);
        for (String student : students) {
            System.out.println(student);

        }

        return "success";
    }

    /**
     * 接收请求头
     * @RequestHeader  用于接收请求头中的所有信息, 会封装到一个Map结构中去
     * @RequestHeader(key)  用于接收请求头中的某一项信息
     * @CookieValue(key)  用于接收cookie中的某一项信息
     */
    @RequestMapping(value = "/demo14")
    public String demo14(
            @RequestHeader Map map,
            @RequestHeader("cookie") String cookie,
            @CookieValue("JSESSIONID") String jsessionid
    ) {
        System.out.println(map);
        System.out.println(cookie);
        System.out.println(jsessionid);
        return "success";
    }


}
