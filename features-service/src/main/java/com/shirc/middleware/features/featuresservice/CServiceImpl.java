package com.shirc.middleware.features.featuresservice;

import com.shirc.middleware.features.featuresfacade.CService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Author shirenchuang
 * @Date 2019/11/30 2:19 PM
 **/
@Service
@Component
public class CServiceImpl implements CService {


    @Override
    public String call() {
        return "===  call C ===";
    }
}
