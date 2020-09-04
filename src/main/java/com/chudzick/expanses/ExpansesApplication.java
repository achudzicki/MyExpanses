package com.chudzick.expanses;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
public class ExpansesApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ExpansesApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
