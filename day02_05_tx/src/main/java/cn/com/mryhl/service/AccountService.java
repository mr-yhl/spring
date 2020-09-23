package cn.com.mryhl.service;

public interface AccountService {
    /**
     * 转账抽象类
     * @param sourceAccountName 转账用户
     * @param targetAccountName 目标用户
     * @param amount 金额
     */
    void transfer(String sourceAccountName,String targetAccountName,Float amount);
}
