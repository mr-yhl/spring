## JdbcTemplate介绍(会用)
[pom.xml](./pom.xml) | [Account](./src/main/java/cn/com/mryhl/domain/Account.java) | 
[applicationContext.xml](./src/main/resources/applicationContext.xml) | [dao文件夹](./src/main/java/cn/com/mryhl/dao) | [service层](./src/main/java/cn/com/mryhl/service) |
 [测试文件](./src/test/java/test/AccountServiceTest.java)

### ### 注解版(重点)
    
    #### 3.1 复制工程
    
    ```xml
    <modules>
        <module>day01_01_ioc</module>
        <module>day01_02_springioc</module>
        <module>day01_03_dbutils</module>
        <module>day01_04_crud</module>
        <module>day02_01_review</module>
        <module>day02_02_anno_demo</module>
        <module>day02_03_crudanno</module>
        <module>day02_04_transfer</module>
        <module>day02_05_tx</module>
        <module>day03_01_tx_jdk</module>
        <module>day03_02_tx_cglib</module>
        <module>day03_03_aop</module>
        <module>day03_04_aop_anno</module>
        <module>day03_05_tx_springaop</module>
        <module>day03_06_jt</module>
        <module>day03_07_jt_tx_xml</module>
        <module>day03_08_jt_tx_anno</module>
    </modules>
    ```
    
     
    
    #### 3.2 删除aop:config和tx:advice的配置
    
    ```xml
    <!--<tx:advice id="txAdvice" transaction-manager="transactionManager">
        <tx:attributes>
            <tx:method name="find*" read-only="true"></tx:method>
            <tx:method name="*"/>
        </tx:attributes>
    </tx:advice>
    <aop:config>
        <aop:pointcut id="pt" expression="execution(* cn.com.mryhl.service.impl.*.*(..))"/>
        <aop:advisor advice-ref="txAdvice" pointcut-ref="pt"></aop:advisor>
    </aop:config>-->
    ```
    
    #### 3.3 开启注解驱动
    
    ```xml
    <!--开启注解驱动-->
    <tx:annotation-driven></tx:annotation-driven>
    ```
    
    ####  3.4 在目标对象中添加事务管理注解
    
    ```java
    @Service
    @Transactional // 表示当前类具有事务管理功能
    public class AccountServiceImpl implements AccountService {
    
        @Autowired
        private AccountDao accountDao;
        @Transactional(
                readOnly = false,
                isolation = Isolation.DEFAULT,
                propagation = Propagation.REQUIRED,
                timeout = -1
        )
        public void transfer(String sourceAccountName, String targetAccountName, Float amount) {
            // 查询两个用户的信息
            Account sourceAccount = accountDao.findByName(sourceAccountName);
            Account targetAccount = accountDao.findByName(targetAccountName);
            // 内存内做金额变更
            sourceAccount.setBalance(sourceAccount.getBalance() - amount);
            targetAccount.setBalance(targetAccount.getBalance() + amount);
    
            // 变更后的金额提交
            accountDao.update(sourceAccount);
            // int i=1/0;
            accountDao.update(targetAccount);
    
        }
    
        public void save(Account account) {
            accountDao.save(account);
        }
    
        public List<Account> findAll() {
            return accountDao.findAll();
        }
    
        public Account findByName(String name) {
            return accountDao.findByName(name);
        }
    
        public void update(Account account) {
            accountDao.update(account);
        }
    
    
        public void deleByName(String name) {
            accountDao.deleByName(name);
        }
    }
    ```
    
     
    
    ### 4. 纯注解版(了解)
    
    #### 4.1转移配置到配置类
    
    ```java
    package cn.com.mryhl.config;
    
    import com.alibaba.druid.pool.DruidDataSource;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.ComponentScan;
    import org.springframework.context.annotation.EnableAspectJAutoProxy;
    import org.springframework.jdbc.core.JdbcTemplate;
    import org.springframework.jdbc.datasource.DataSourceTransactionManager;
    
    
    import javax.sql.DataSource;
    @ComponentScan("cn.com.mryhl")
    @EnableAspectJAutoProxy
    public class SpringConifg {
        @Bean
        public DataSource dataSource(){
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql:///spring");
            dataSource.setUsername("root");
            dataSource.setPassword("root");
            return dataSource;
        }
    
        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource){
            return new JdbcTemplate(dataSource);
        }
    
        @Bean
        public DataSourceTransactionManager transactionManager(DataSource dataSource){
            DataSourceTransactionManager manager = new DataSourceTransactionManager();
            manager.setDataSource(dataSource);
            return manager;
        }
    }
    ```
    
     
    
    ####  4.2 测试
    
    ```java
    /**
     * 引入spring测试注解
     */
    @RunWith(SpringJUnit4ClassRunner.class)
    // @ContextConfiguration("classpath:applicationContext.xml")
    @ContextConfiguration(classes= SpringConifg.class)
    public class AccountServiceTest {
        @Autowired
        private AccountService accountService;
    
        /**
         *
         */
        @Test
        public void test01(){
            accountService.transfer("B01","B02",10f);
        }
    
    }
    ```
    
     
