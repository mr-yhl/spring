package cn.com.mryhl.service.impl;


import cn.com.mryhl.dao.AccountDao;
import cn.com.mryhl.domain.Account;
import cn.com.mryhl.service.AccountService;
import cn.com.mryhl.tx.TxManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private TxManager txManager;

    public void transfer(String sourceAccountName, String targetAccountName, Float amount) {
        try {
            // 事务开启
            txManager.begin();
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
    }
}
