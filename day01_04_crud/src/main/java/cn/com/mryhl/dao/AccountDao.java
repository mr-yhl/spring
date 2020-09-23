package cn.com.mryhl.dao;

import cn.com.mryhl.domain.Account;

import java.util.List;

public interface AccountDao {
    /**
     * 保存
     */
    void save(Account account);

    /**
     * 查询所有
     */
    List<Account> findAll();

    /**
     * 根据name查询
     */
    Account findByName(String name);

    /**
     * 根据name修改
     */
    void update(Account account);

    /**
     * 删除
     */
    void deleByName(String name);
}
