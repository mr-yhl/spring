[pow.xml](./pom.xml) | [applicationContext.xml](./src/main/resources/applicationContext.xml) | [log文件](./src/main/java/cn/com/mryhl/log/Logger.java) | [service层](./src/main/java/cn/com/mryhl/service) |
  [测试文件](./src/test/java/cn/com/mryhl/test/AccountServiceTest.java) | [java配置文件](./src/main/java/cn/com/mryhl/config/SpringConfig.java)
##  SpringAOP注解版(重点)
    
    ### 1. 环境准备
    
    #### 1.1 复制工程
    
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
        <!--引入新的文件-->
        <module>day03_03_aop_anno</module>
    </modules>
    ```
    
     
    
    #### 1.2 添加组件扫描
    
    ```xml
    <!--组件扫描-->
    <context:component-scan base-package="cn.com.mryhl"></context:component-scan>
    
    <!--目标对象-->
    <!--<bean id="accountService" class="cn.com.mryhl.service.impl.AccountServiceImpl"></bean>-->
    <!--增强对象-->
    <!--<bean id="logger" class="cn.com.mryhl.log.Logger"></bean>-->
    ```
    
     
    
    #### 1.3 将AccountServiceImpl变成注解
    
    ```java
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
    ```
    
     
    
    #### 1.4 将Logger变成注解
    
    ```java
    /**
     * 增强方法,打印日志
     * 加入注解
     */
    @Component
    public class Logger {
        /**
         * 环绕通知
         */
        public void aroundMethod(ProceedingJoinPoint pjp){
            try {
                System.out.println("即将执行方法.......");
    
                //切点执行
                pjp.proceed();
    
                System.out.println("方法正常结束.......");
    
            }catch (Throwable e){
                e.printStackTrace();
                System.out.println("方法出现异常.......");
    
            }finally {
                System.out.println("方法运行到最后.......");
            }
        }
        /**
         * 被增强方法执行前
         */
        public void beforeMethod(){
            System.out.println(new Date()+"方法开始执行了..");
        }
    
        /**
         * 被增强方法正常执行完成
         */
        public void afterReturnMethod(){
            System.out.println(new Date()+"方法执行完成了..");
        }
    
        /**
         * 被增强方法出现异常
         */
        public void afterThrowMethod(){
            System.out.println(new Date()+"方法出现异常了..");
        }
        /**
         * 被增强方法到最后
         */
        public void afterMethod(){
            System.out.println(new Date()+"方法执行到最后..");
        }
    }
    ```
    
     
    
    ### 2.  四大通知
    
    >将切面转移到增强类上
    
    #### 2.1 激活切面自动代理
    
    ```xml
    <!--激活切面自动代理-->
    <aop:aspectj-autoproxy></aop:aspectj-autoproxy>
    ```
    
    ####  2.2 定义切面
    
    ```java
    /**
     * 增强方法,打印日志
     * 加入注解
     * @Aspect声明这是一个切面
     */
    @Component
    @Aspect
    public class Logger {
        /**
         * 定义切面表达式
         */
        @Pointcut("execution(* cn.com.mryhl.service.impl.*.*(..))")
        public void pt(){};
        /**
         * 环绕通知
         */
        public void aroundMethod(ProceedingJoinPoint pjp){
            try {
                System.out.println("即将执行方法.......");
    
                //切点执行
                pjp.proceed();
    
                System.out.println("方法正常结束.......");
    
            }catch (Throwable e){
                e.printStackTrace();
                System.out.println("方法出现异常.......");
    
            }finally {
                System.out.println("方法运行到最后.......");
            }
        }
        /**
         * 被增强方法执行前
         */
        @Before("pt()")
        public void beforeMethod(){
            System.out.println(new Date()+"方法开始执行了..");
        }
    
        /**
         * 被增强方法正常执行完成
         */
        public void afterReturnMethod(){
            System.out.println(new Date()+"方法执行完成了..");
        }
    
        /**
         * 被增强方法出现异常
         */
        public void afterThrowMethod(){
            System.out.println(new Date()+"方法出现异常了..");
        }
        /**
         * 被增强方法到最后
         */
        public void afterMethod(){
            System.out.println(new Date()+"方法执行到最后..");
        }
    }
    ```
    
     2.3 测试
    
    ```java
    public class AccountServiceTest {
        public static void main(String[] args) {
            ApplicationContext act = new ClassPathXmlApplicationContext("applicationContest.xml");
    
            AccountService accountService = act.getBean(AccountService.class);
            accountService.findAll();
        }
    }
    ```
    
    > Fri Sep 25 19:58:38 CST 2020方法开始执行了..
    > findAll
    
    **四大通知同时出现的时候,注解版会有顺序问题, 不能用**
    
    ### 3. 环绕通知
    
    ```java
    /**
     * 环绕通知
     */
    @Around("pt()")
    public void aroundMethod(ProceedingJoinPoint pjp){
        try {
            System.out.println("即将执行方法.......");
    
            //切点执行
            pjp.proceed();
    
            System.out.println("方法正常结束.......");
    
        }catch (Throwable e){
            e.printStackTrace();
            System.out.println("方法出现异常.......");
    
        }finally {
            System.out.println("方法运行到最后.......");
        }
    }
    ```
    
    
    > 即将执行方法.......
    > 		findAll
    > 		方法正常结束.......
    > 		方法运行到最后.......
    
    ### 4. 纯注解版
    
    #### 4.1 提取配置
    
    ```java
    @ComponentScan("cn.com.mryhl")
    /**
     * 自动激活代理
     */
    @EnableAspectJAutoProxy 
    public class SpringConfig {
    }
    ```
    
     
    
    #### 4.2测试
    
    ```java
    public class AccountServiceTest {
        public static void main(String[] args) {
            // ApplicationContext act = new ClassPathXmlApplicationContext("applicationContest.xml");
            ApplicationContext act = new AnnotationConfigApplicationContext(SpringConfig.class);
    
            AccountService accountService = act.getBean(AccountService.class);
            accountService.findAll();
        }
    }
    ```
