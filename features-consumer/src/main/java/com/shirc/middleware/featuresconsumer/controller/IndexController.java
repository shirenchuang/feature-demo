package com.shirc.middleware.featuresconsumer.controller;

import com.shirc.middleware.features.featuresfacade.AService;
import com.shirc.middleware.features.featuresfacade.BService;
import com.shirc.middleware.features.featuresfacade.CService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shirenchuang
 * @date 2019/12/5  4:43 下午
 */

@RestController
@RequestMapping(value = "/index")
public class IndexController {

    @Reference
    private AService aService;

    @Reference
    private BService bService;

    @Reference
    private CService cService;

    @GetMapping
    public String get(String call){

        if("a".equals(call)){
            return aService.call();
        }else if("b".equals(call)){
            return bService.call();
        }else if("c".equals(call)) {
            return cService.call();
        }else if("abc".equals(call)){
            return aService.A_callB_C();
        }

        else {
            return "";
        }
    }

}
