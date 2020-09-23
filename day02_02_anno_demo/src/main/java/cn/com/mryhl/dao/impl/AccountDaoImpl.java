package cn.com.mryhl.dao.impl;

import cn.com.mryhl.dao.AccountDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

//@Component 这个注解翻译为组件, 只要Spring发现一个类个对象, 并将对象放入自己的IOC容器中,
//他就会利用当前类反射创建出一dao.impl.AccountDaoImpl"></bean>
//相当于XML中<bean id="accountDao" class="com.itheima.上面标注了次注解,
//它默认的id是当前类名首字母小写(accountDaoImpl), 也支持使用一个value属性自定义这个id名称
//@Component("accountDao")
//@Scope("singleton") // 指定当前对象为单例(默认)
//@Scope("prototype") // 指定当前对象为多例
@Repository
public class AccountDaoImpl implements AccountDao {
    public void save() {
        System.out.println("保存成功....");
    }
    @PostConstruct
    public void init(){
        System.out.println("创建之后");
    }
    @PreDestroy
    public void des(){
        System.out.println("销毁前");
    }
}
