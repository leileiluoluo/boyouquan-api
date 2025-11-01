package com.boyouquan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@MapperScan(basePackages = {"com.boyouquan.dao"})
public class BoYouQuanWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoYouQuanWebApplication.class, args);
    }

}
