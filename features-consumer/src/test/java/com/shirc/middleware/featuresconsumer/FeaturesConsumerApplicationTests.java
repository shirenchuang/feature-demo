package com.shirc.middleware.featuresconsumer;

import com.shirc.middleware.features.featuresfacade.AService;
import com.shirc.middleware.features.featuresfacade.BService;
import com.shirc.middleware.features.featuresfacade.CService;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class FeaturesConsumerApplicationTests {


    @Reference
    private CService cService;

    @Reference
    private AService aService;

    @Reference
    private BService bService;

    @Test
    public void contextLoads() {
        String s = cService.call();
        System.out.println(s);
    }

}
