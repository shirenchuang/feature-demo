package com.shirc.middleware.featuresconsumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.shirc.middleware.features.featuresfacade.SmsService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class FeaturesConsumerApplicationTests {


    @Reference
    private SmsService smsService;

    @Test
    public void contextLoads() {
        String s = smsService.sendSms("13111111111", "hello world");
        System.out.println(s);
    }

}
