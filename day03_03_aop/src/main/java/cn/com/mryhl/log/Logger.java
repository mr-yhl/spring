package cn.com.mryhl.log;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Date;

/**
 * 增强方法,打印日志
 */
public class Logger {
    /**
     * 环绕通知
     */
    public void aroundMethod(ProceedingJoinPoint pjp){
        try {
            System.out.println("即将执行方法.......");

            //切点执行
            pjp.proceed();

            System.out.println("方法正常结束.......");

        }catch (Throwable e){
            e.printStackTrace();
            System.out.println("方法出现异常.......");

        }finally {
            System.out.println("方法运行到最后.......");
        }
    }
    /**
     * 被增强方法执行前
     */
    public void beforeMethod(){
        System.out.println(new Date()+"方法开始执行了..");
    }

    /**
     * 被增强方法正常执行完成
     */
    public void afterReturnMethod(){
        System.out.println(new Date()+"方法执行完成了..");
    }

    /**
     * 被增强方法出现异常
     */
    public void afterThrowMethod(){
        System.out.println(new Date()+"方法出现异常了..");
    }
    /**
     * 被增强方法到最后
     */
    public void afterMethod(){
        System.out.println(new Date()+"方法执行到最后..");
    }
}
