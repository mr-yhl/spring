package cn.com.mryhl.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * Java文件作为配置文件
 * 注解版的组件扫描,相当于XML中<context:component-scan base-package="com.itheima"/>
 * bean标签---方法     非bean标签---新注解
 */
@ComponentScan("cn.com.mryhl")
// @Import(DbConfig.class)
public class SpringConfig {


    @Bean
    public QueryRunner queryRunner(DataSource dataSource){
        return new QueryRunner(dataSource);
    }

}
