package com.atguigu.gmall.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.atguigu.gmall.manage.mapper")
@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy = true)
public class ManageServiceMain8071 {

    public static void main(String[] args) {
        SpringApplication.run(ManageServiceMain8071.class,args);
    }
}
