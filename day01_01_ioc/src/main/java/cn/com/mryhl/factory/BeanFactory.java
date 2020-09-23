package cn.com.mryhl.factory;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class BeanFactory {
    // 希望对象是提前创建的,保存在一个位置 调用时直接给出去
    private static Map<String,Object> map = new HashMap<String, Object>();

    static {
        try {
            // 读取配置文件
            ResourceBundle rb = ResourceBundle.getBundle("beans");
            Enumeration<String> keys = rb.getKeys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                // 获取类的权限定名
                String className = rb.getString(key);
                // 反射创建对象
                Class<?> clazz = Class.forName(className);
                Object instance = clazz.newInstance();
                map.put(key,instance);
            }
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }

    }

    // 创建对象
    public static Object getBean(String beanId){

       return map.get(beanId);
    }
}
