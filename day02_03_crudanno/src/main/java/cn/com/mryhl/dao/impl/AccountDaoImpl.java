package cn.com.mryhl.dao.impl;

import cn.com.mryhl.dao.AccountDao;
import cn.com.mryhl.domain.Account;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class AccountDaoImpl implements AccountDao {
    @Autowired
    private QueryRunner queryRunner;

    /*public void setQueryRunner(QueryRunner queryRunner) {
        this.queryRunner = queryRunner;
    }*/

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
