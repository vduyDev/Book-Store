package com.example.borrowingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaAuditing
@ComponentScan({"com.example.common","com.example.borrowingservice"})
public class BorrowingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BorrowingServiceApplication.class, args);
    }

}
