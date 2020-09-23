package cn.com.mryhl.dao.impl;

import cn.com.mryhl.dao.AccountDao;
import cn.com.mryhl.domain.Account;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Repository
public class AccountDaoImpl implements AccountDao {

    @Autowired
    private QueryRunner queryRunner;


    public Account findByName(String name) {

        try {
            return queryRunner.query("select * from account where name = ?",new BeanHandler<Account>(Account.class),name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void update(Account account) {
        try {
            queryRunner.update("update account set balance = ? where name = ?",account.getBalance(),account.getName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
