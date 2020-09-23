package cn.com.mryhl.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
//@Configuration 此注解标注在一个类上,代表此类是一个配置类
//Spring在IOC容器启动的时候,会自动扫描所有的配置类, 然后会自动执行类中的方法
@Configuration
@PropertySource("db.properties")
public class DbConfig {
    //@Value 用于给类中的简单类型的属性进行依赖注入
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    //@Bean 只能标注方法上, 作用是将当前方法的返回值对象,放入Spring的IOC容器
    //@Bean 放入到容器的对象的默认id为当前方法的名字(getDataSource),也可以通过@Bean的value属性指定
    //@Bean还具有@Autowired的标注在方法上的时候的所有功能
    //如果当前方法需要参数,那么他会自动从Spring的IOC容器中查找,查找顺序跟@Autowired一致
    @Bean("datasource")
    public DruidDataSource getDataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }
}
