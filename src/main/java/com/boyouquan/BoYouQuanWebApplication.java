package com.boyouquan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.boyouquan.repository")
public class BoYouQuanWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoYouQuanWebApplication.class, args);
    }

}
