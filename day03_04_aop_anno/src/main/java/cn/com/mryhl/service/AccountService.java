package cn.com.mryhl.service;

import java.util.List;

/**
 * 创建service接口
 */
public interface AccountService {
    /**
     * 插入内容方法
     */
    void save(Object o);
    /**
     * 查询所有
     */
    List<Object> findAll();
    /**
     * 通过姓名查找
     */
    Object findByName(String name);

}
