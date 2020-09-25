package cn.com.mryhl.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 增强方法,打印日志
 * 加入注解
 * @Aspect声明这是一个切面
 */
@Component
@Aspect
public class Logger {
    /**
     * 定义切面表达式
     */
    @Pointcut("execution(* cn.com.mryhl.service.impl.*.*(..))")
    public void pt(){};
    /**
     * 环绕通知
     */
    @Around("pt()")
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
    // @Before("pt()")
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
