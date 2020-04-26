package com.atguigu.gmall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.atguigu.gmall.member.mapper")
public class MemberMain8080 {

    public static void main(String[] args) {
        SpringApplication.run(MemberMain8080.class,args);
    }
}
