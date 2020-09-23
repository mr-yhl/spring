package cn.com.mryhl.service.impl;

import cn.com.mryhl.dao.AccountDao;
import cn.com.mryhl.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    //@Autowired
   /* 表示自动依赖注入,它可以标注在属性上,也可以标注在属性对应的set方法上
    当它标注在属性上的时候,set方法可以省略不写

    当Spring发现一个类中的属性上标注了 @Autowired  注解的时候, 它会
    根据当前属性的类型[ AccountDao ] 在Spring的IOC容器中进行查找
     找不到 就报错 [ expected at least 1 bean which qualifies as autowire candidate.]
     找到了一个  就进行依赖注入
     找到了多个  它会按照当前属性的名字[accountDao]  进行过滤
          如果可以过滤出来,就进行依赖注入
          如果可以过滤出来,就报错[ expected single matching bean but found 2: accountDaoImpl1,accountDaoImpl2 ]
    */
    @Autowired
    //@Qualifier("accountDao1") 它的作用是配合@Autowired使用, 用于在多个匹配中的类型中按照id选中其中的一个
    @Qualifier("accountDao1")
    private AccountDao accountDao;


    /**
     * 注入对象
     *
     */
    /*public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }*/

    public void save() {
        accountDao.save();
    }
}
