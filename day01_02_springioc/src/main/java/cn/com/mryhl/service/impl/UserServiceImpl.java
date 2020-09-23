package cn.com.mryhl.service.impl;

import cn.com.mryhl.dao.UserDao;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserServiceImpl {
    @Test
    public void save(){
        // 1.读取配置文件,启动spring的IOC容器
        ClassPathXmlApplicationContext act = new ClassPathXmlApplicationContext("applicationContext.xml");
        // FileSystemXmlApplicationContext act = new FileSystemXmlApplicationContext("F:\\code\\spring\\day01_02_springioc\\src\\main\\resources\\applicationContext.xml");
        // 2.从容器中获取对象
        // 根据id获取对象
        // UserDao userDao = (UserDao) act.getBean("userDao");
        // 根据class获取
        // UserDao userDao = act.getBean(UserDao.class);
        // 根据id和class获取
        UserDao userDao = act.getBean("userDao",UserDao.class);
        // 3.调用对象的方法
        userDao.save();

    }
}
