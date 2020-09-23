### DbUtils介绍
[Account实体](./src/main/java/cn/com/mryhl/domain/Account.java)

[DbutilsTest测试类](./src/test/java/cn/com/mryhl/test/DbutilsTest.java)

> java中操作数据库的框架，一定会有的方法：
>
> 	1. 发送SQL语句
>
>    	2. 接收返回的数据结果，并且封装为对象或集合

DbUtils是Apache的一款用于简化Dao代码的工具类，它底层封装了JDBC技术。

核心类：

- QueryRunner          用于执行增删改查的SQL语句
- ResultSetHandler    这是一个接口，主要作用是将数据库返回的记录封装进实体对象或集合

核心方法：

- update()    用来执行增、删、改语句  
- query()      用来执行查询语句

```java
//创建一个QueryRunner对象，用来执行增删改查
//这里需要给一个数据源，如果此处不给，那么使用它调用具体API的时候必须要给
QueryRunner queryRunner = new QueryRunner(dataSource);

//update方法，用于执行增删改语句
//第一个参数:sql语句   后面的参数:sql语句中的所需要的的值
queryRunner.update("insert into account value(null,?,?)",1,2);

//query方法，用于执行查询语句
//第一个参数:sql语句   第一个参数:封装返回值   后面的参数:sql语句中的所需要的的值
//BeanHandler用于将一条返回数据封装成一个JavaBean,  类似的子类还有BeanListHandler等
queryRunner.query("select * from account where aid = ?",  new BeanHandler<Account>(Account.class),  1); 
```