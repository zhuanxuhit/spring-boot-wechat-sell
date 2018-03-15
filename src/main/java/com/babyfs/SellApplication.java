package com.babyfs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SellApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SellApplication.class);
        context.close();
    }
}
