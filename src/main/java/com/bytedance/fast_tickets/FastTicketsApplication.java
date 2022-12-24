package com.bytedance.fast_tickets;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.bytedance.fast_tickets.dao")
@EnableCaching
@EnableTransactionManagement
public class FastTicketsApplication {
    public static void main(String[] args) {
        SpringApplication.run(FastTicketsApplication.class, args);
    }

}
