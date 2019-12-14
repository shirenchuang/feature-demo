package com.shirc.middleware.features.featuresservice;

import com.daimler.cap.discovery.Discovery;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDubbo
public class FeaturesServiceApplication {


    @Bean
    public Discovery discovery() {
        Discovery discovery =  new Discovery("features-demo");
        discovery.setZkaddress("127.0.0.1");
        return discovery;
    }

    public static void main(String[] args) {
        SpringApplication.run(FeaturesServiceApplication.class, args);
    }

}
