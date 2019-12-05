package com.shirc.middleware.features.featuresservices2;

import com.shirc.middleware.features.featuresfacade.BService;
import com.shirc.middleware.features.featuresfacade.CService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

/**
 * @author shirenchuang
 * @date 2019/12/5  4:40 下午
 */

@Service
@Component
public class BServiceImpl implements BService {

    @Reference
    private CService cService;

    @Override
    public String call() {
        return "===  call B ===";
    }

    @Override
    public String B_call_C() {
        return call()+"》"+cService.call();
    }


}
