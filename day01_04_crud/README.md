使用Spring完成CRUD操作(重点)
## 数据库
### 

```sql
CREATE TABLE account(
	aid INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(100) NOT NULL UNIQUE,
	balance FLOAT(10,2)
)
```
### 引入xml文件
[pom.xml](./pom.xml)

### 实体类
[Account](./src/main/java/cn/com/mryhl/domain/Account.java)

### dao接口实现类
[接口](./src/main/java/cn/com/mryhl/dao/AccountDao.java)  | [实现类](./src/main/java/cn/com/mryhl/dao/impl/AccountDaoImpl.java)

### service接口实现类
[接口](./src/main/java/cn/com/mryhl/service/AccountService.java)  | [实现类](./src/main/java/cn/com/mryhl/service/impl/AccountServiceImpl.java)

### 测试
[测试](./src/test/java/cn/com/mryhl/test/AccountServiceTest.java)