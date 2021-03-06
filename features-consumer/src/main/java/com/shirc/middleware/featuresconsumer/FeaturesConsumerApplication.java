package com.shirc.middleware.featuresconsumer;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class FeaturesConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeaturesConsumerApplication.class, args);
    }

}
