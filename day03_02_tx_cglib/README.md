##  cglib动态代理(了解)
[pow.xml](./pom.xml) | [applicationContext.xml](./src/main/resources/applicationContext.xml) | [dao文件夹](./src/main/java/cn/com/mryhl/dao) | [service层](./src/main/java/cn/com/mryhl/service) |
[tx文件](./src/main/java/cn/com/mryhl/tx/TxManager.java) | [测试文件](./src/test/java/cn/com/mryhl/test/AccountServiceTest.java)
### 1复制工程

> 修改项目的pom.xml文件,引入复制的工程

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
    <!--新引入的项目-->
    <module>day03_02_tx_cglib</module>
</modules>
```

###  2去掉所有跟service接口相关的内容

> 删除service接口,及其相关的引用
>
> 1. 其实现类的实现引入
> 2. 测试文件中的多态引入

### 3使用cglib创建代理对象

> 遇见问题
>
> Error:(67, 30) java: 不兼容的类型: java.lang.reflect.InvocationHandler无法转换为org.springframework.cglib.proxy.Callback
>
> 解决办法,重新导入InvocationHandler 所在的包
>
> import java.lang.reflect.InvocationHandler; -->
>
> import org.springframework.cglib.proxy.InvocationHandler;

```java
/**
 * 引入spring测试注解
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class AccountServiceTest {
    @Autowired
    private AccountServiceImpl accountService;

    /**
     * 引入事务
     */
    @Autowired
    private TxManager txManager;
    /**
     * 进行测试
     */
    @Test
    public void testTransfer()  {
        // 编写代理逻辑
        InvocationHandler invocationHandler = new InvocationHandler(){
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 创建返回对象
                Object obj = null;
                try {
                    // 事务开启
                    txManager.begin();
                    // 查询两个用户的信息
                    obj = method.invoke(accountService,args);
                    // 事务提交
                    txManager.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 事务回滚
                    txManager.rollback();
                } finally {
                    // 事务关闭
                    txManager.close();
                }

                return obj;
            }
        };
        // 使用Cglib的动态代理产生代理对象
        // 1. 创建一个增强器
        Enhancer enhancer = new Enhancer();
        // 2. 设置父类
        enhancer.setSuperclass(AccountServiceImpl.class);
        // 3. 设置代理逻辑
        enhancer.setCallback(invocationHandler);
        // 4. 产生代理对象
        AccountServiceImpl instance = (AccountServiceImpl) enhancer.create();


        instance.transfer("B01","B02",10f);
    }
}
```



### 4 jdk和cglib两种代理方式的选择(面试)

首先明确在创建代理实现类时，jdk的速度要高于cglib，所以选择的时候:

- 当被代理类有接口的时候，使用jdk动态代理，因为它的效率高

- 当被代理类没有接口的时候，使用cglib动态代理，因为没办法

  

### 5. 总结 

当核心业务（转账）和增强业务（事务）同时出现时，我们可以在开发时对他们分别开发，运行时再组装在一起（使用动态代理的方式）。

这样做的好处是：

1. 逻辑清晰，开发核心业务的时候，不必关注增强业务的代码
2. 代码复用性高：增强代码不用重复书写

这就是一种 AOP 的思想。 

我的总结:  **开发阶段分别开发  运行阶段组装运行**