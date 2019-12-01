package com.shirc.middleware.features.featuresservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class FeaturesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeaturesServiceApplication.class, args);
    }

}
