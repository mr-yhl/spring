package cn.com.mryhl.service.impl;

import cn.com.mryhl.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 实现类
 * 加入注解
 */
@Service
public class AccountServiceImpl implements AccountService {
    public void save(Object o) {
        System.out.println("save");

    }

    public List<Object> findAll() {
        System.out.println("findAll");
        return null;
    }

    public Object findByName(String name) {
        System.out.println("findByName");
        return null;
    }
}
