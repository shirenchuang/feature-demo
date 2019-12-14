package com.shirc.middleware.features.featuresservice;

import com.daimler.cap.annotation.API;
import com.daimler.cap.annotation.Application;
import com.daimler.cap.annotation.Param;
import com.daimler.cap.annotation.ParamInfo;
import com.shirc.middleware.features.featuresfacade.AService;
import com.shirc.middleware.features.featuresfacade.BService;
import com.shirc.middleware.features.featuresfacade.CService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Author shirenchuang
 * @Date 2019/11/30 2:19 PM
 **/
@Application("features-demo")
@Service
@Component
public class AServiceImpl implements AService {

    @Reference
    private BService bService;


    @Override
    public String call() {
        return "===  call A ===";
    }

    @Override
    public String A_callB_C(){
        return "===  call A ===》"+bService.B_call_C();
    }


    @API
    @Override
    public String featureDemo(){
        return "。。。。。";
    }
}
