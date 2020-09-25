# Spring学习项目
## [引入IOC](./day01_01_ioc)
本项目引入了ioc的概念.
[说明文件](./day01_01_ioc/README.md)
## [spring入门案例](./day01_02_springioc) 
本项目主要练习了spring入门的基础内容
[说明文件](./day01_02_springioc/README.md)
## [DbUtils入门案例](./day01_03_dbutils)
本项目主要练习的是spring自带的DbUtils.
[说明文件](./day01_03_dbutils/README.md)
## [crud 练习](./day01_04_crud)
练习了spring的crud
[说明文件](./day01_04_crud/README.md)
## [spring回顾](./day02_01_review)
本项目主要是回顾一下如何创建spring项目.
[说明文件](./day02_01_review/README.md)
## [Spring注解](./day02_02_anno_demo)
本项目引入了spring的注解.
[说明文件](./day02_01_review/README.md)
## [spring常用注解开发、存注解](./day02_03_crudanno)
本项目练习了spring常用的注解开发方式,也练习了存注解版本.
[说明文件](./day02_03_crudanno/README.md)
## [完成转账功能一](./day02_04_transfer)
实现了转账功能,但是还存在一些问题.
[说明文件](./day02_04_transfer/README.md)
## [解决事务问题](./day02_05_tx)
解决事务问题
[说明文件](./day02_05_tx/README.md)
## [使用动态代理优化转账代码jdk](./day03_01_tx_jdk)
使用动态代理的方式优化了转账的事务
[说明文件](./day03_01_tx_jdk/README.md)
## [使用动态代理优化转账代码cglib](./day03_02_tx_cglib)
使用动态代理的方式优化了转账的事务
[说明文件](./day03_02_tx_cglib/README.md)
## [SpringAOP的入门案例(重点)](./day03_03_aop)
练习了AOP的入门案例,说明了一些术语与概念
[说明文件](./day03_03_aop/README.md)
## [SpringAOP注解版(重点)](./day03_04_aop_anno)
练习了AOP的注解开发
[说明文件](./day03_04_aop_anno/README.md)
## [SpringAOP实现事务管理(重点)](./day03_05_tx_springaop)
SpringAOP实现事务管理
[说明文件](./day03_05_tx_springaop/README.md)
## [JdbcTemplate](./day03_06_jt)
JdbcTemplate是Spring提供的持久层技术，用于操作数据库，它底层封装了JDBC技术。
[说明文件](./day03_06_jt/README.md)
## [声明式事务](./day03_07_jt_tx_xml)
目标对象:  业务层的所有对象   **自己开发**

增强对象:  事务管理器 (DataSourceTransactionManager) 

​                **需要我们给它传递一批参数(事务隔离级别  传播行为  超时时长  只读)**

配置切面:  配置的是切点和增强的关系   **自己配置**
[说明文件](./day03_07_jt_tx_xml/README.md)
## [声明式事务注解](./day03_08_jt_tx_anno)
目标对象:  业务层的所有对象   **自己开发**

增强对象:  事务管理器 (DataSourceTransactionManager) 

​                **需要我们给它传递一批参数(事务隔离级别  传播行为  超时时长  只读)**

配置切面:  配置的是切点和增强的关系   **自己配置**
[说明文件](./day03_08_jt_tx_anno/README.md)