package cn.com.mryhl.dao;

import cn.com.mryhl.domain.Account;

public interface AccountDao {
    /**
     * 查询
     */
    Account findByName(String name);
    /**
     * 更新
     */
    void update(Account account);
}
