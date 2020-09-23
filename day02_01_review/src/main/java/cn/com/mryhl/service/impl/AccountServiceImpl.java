package cn.com.mryhl.service.impl;

import cn.com.mryhl.dao.AccountDao;
import cn.com.mryhl.service.AccountService;

public class AccountServiceImpl implements AccountService {
    private AccountDao accountDao;


    /**
     * 注入对象
     * @param accountDao
     */
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void save() {
        accountDao.save();
    }
}
