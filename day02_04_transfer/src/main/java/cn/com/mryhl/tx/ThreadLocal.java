package cn.com.mryhl.tx;

import java.util.Hashtable;
import java.util.Map;

/**
 * 本地集合
 */
public class ThreadLocal {
    private Map<Thread,String> map = new Hashtable<Thread, String>();

    /**
     * 保存
     */
    public void set(String value){
        map.put(Thread.currentThread(),value);
    }

    /**
     * 获取
     */
    public String get(){
        return map.get(Thread.currentThread());
    }
    /**
     * 清理
     */
    public String remove(){
        return map.remove(Thread.currentThread());
    }
}
