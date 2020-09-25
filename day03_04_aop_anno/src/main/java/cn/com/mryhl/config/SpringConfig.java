package cn.com.mryhl.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@ComponentScan("cn.com.mryhl")
/**
 * 自动激活代理
 */
@EnableAspectJAutoProxy
public class SpringConfig {
}
