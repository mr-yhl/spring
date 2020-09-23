package cn.com.mryhl.service.impl;

import cn.com.mryhl.dao.AccountDao;
import cn.com.mryhl.domain.Account;
import cn.com.mryhl.service.AccountService;

import java.util.List;

public class AccountServiceImpl implements AccountService {
    //希望Spring给我们注入这个对象
    private AccountDao accountDao;
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
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
