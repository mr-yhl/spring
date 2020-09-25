package cn.com.mryhl.service.impl;

import cn.com.mryhl.dao.AccountDao;
import cn.com.mryhl.domain.Account;
import cn.com.mryhl.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    public void transfer(String sourceAccountName, String targetAccountName, Float amount) {
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

    }

    public void save(Account account) {
        accountDao.save(account);
    }

    public List<Account> findAll() {
        return accountDao.findAll();
    }

    public Account findByName(String name) {
        return accountDao.findByName(name);
    }

    public void update(Account account) {
        accountDao.update(account);
    }


    public void deleByName(String name) {
        accountDao.deleByName(name);
    }
}
