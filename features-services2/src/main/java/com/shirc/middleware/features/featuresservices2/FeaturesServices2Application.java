package com.shirc.middleware.features.featuresservices2;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class FeaturesServices2Application {

    public static void main(String[] args) {
        SpringApplication.run(FeaturesServices2Application.class, args);
    }

}
