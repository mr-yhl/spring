package cn.com.mryhl.test;

import cn.com.mryhl.domain.Account;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.Test;

import java.util.List;

/**
 * 测试类
 */
public class DbutilsTest {
    /**
     * 新增测试
     */
    @Test
    public void testSave() throws Exception {
        // 1.创建DataSourse{驱动 连接 用户名 密码}
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql:///spring");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        // 2.创建QueryRunner对象
        QueryRunner queryRunner = new QueryRunner(dataSource);
        // 3.发生一条sql语句
        queryRunner.update("insert into account values (null,?,?)","B01",100f);
    }

    /**
     * 新增测试
     */
    @Test
    public void testFindAll() throws Exception {
        // 1.创建DataSourse{驱动 连接 用户名 密码}
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql:///spring");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        // 2.创建QueryRunner对象
        QueryRunner queryRunner = new QueryRunner(dataSource);
        // 3.发生一条sql语句
        List<Account> accountList = queryRunner.query("select * from account", new BeanListHandler<Account>(Account.class));
        for (Account account : accountList) {
            System.out.println(account);
        }
    }

    /**
     * 新增测试
     */
    @Test
    public void testFindById() throws Exception {
        // 1.创建DataSourse{驱动 连接 用户名 密码}
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql:///spring");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        // 2.创建QueryRunner对象
        QueryRunner queryRunner = new QueryRunner(dataSource);
        // 3.发生一条sql语句
        Account account = queryRunner.query("select * from account where aid = ?", new BeanHandler<Account>(Account.class),1);

        System.out.println(account);

    }
}
