package com.leftoverfood;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.leftoverfood.mapper")
public class LeftoverFoodApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeftoverFoodApplication.class, args);
    }
}
