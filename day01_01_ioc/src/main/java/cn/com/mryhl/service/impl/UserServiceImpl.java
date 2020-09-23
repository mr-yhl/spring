package cn.com.mryhl.service.impl;

import cn.com.mryhl.dao.UserDao;
import cn.com.mryhl.dao.impl.UserDaoImpl;
import org.junit.Test;

public class UserServiceImpl {
    @Test
    public void save(){
        UserDao userDao = new UserDaoImpl();
        userDao.save();
    }
}
