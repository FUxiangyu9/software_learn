package com.ctsw.recruit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//  启动类

@SpringBootApplication
@MapperScan("com.ctsw.recruit.mapper")
public class RecruitApplication {

    public static void main(String[] args) {

        SpringApplication.run(RecruitApplication.class, args);
    }

}
