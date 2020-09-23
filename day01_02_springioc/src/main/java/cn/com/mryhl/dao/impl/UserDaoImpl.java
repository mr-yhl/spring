package cn.com.mryhl.dao.impl;

import cn.com.mryhl.dao.UserDao;

public class UserDaoImpl implements UserDao {
    @Override
    public void save() {
        System.out.println("保存成功...");
    }
}
