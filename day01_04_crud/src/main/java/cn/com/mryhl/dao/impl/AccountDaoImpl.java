package cn.com.mryhl.dao.impl;

import cn.com.mryhl.dao.AccountDao;
import cn.com.mryhl.domain.Account;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public class AccountDaoImpl implements AccountDao {
    // 我们需要用 QueryRunner 操作数据库, 就要获取到QueryRunner, 而用了spring之后,不允许我们自己new 只能问spring要
    // Spring会给我们进行赋值, 这个过程就称为依赖注入(DI) , DI有两种方式, 我们推荐使用set方式
    // 必须提供属性set方法

    //=========希望Spring给我们注入这个对象==============
    private QueryRunner queryRunner;

    public void setQueryRunner(QueryRunner queryRunner) {
        this.queryRunner = queryRunner;
    }

    public void save(Account account) {
        try {
            queryRunner.update("insert into account values(null,?,?)", account.getName(), account.getBalance());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Account> findAll() {
        try {
            return queryRunner.query("select * from account", new BeanListHandler<Account>(Account.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account findByName(String name) {
        try {
            return queryRunner.query("select * from account where name = ?", new BeanHandler<Account>(Account.class), name);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void update(Account account) {
        try {
            queryRunner.update("update account set balance = ? where name = ?", account.getBalance(), account.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleByName(String name) {
        try {
            queryRunner.update("delete from account where name = ?", name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
