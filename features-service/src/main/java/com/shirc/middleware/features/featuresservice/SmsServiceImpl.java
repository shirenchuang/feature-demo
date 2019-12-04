package com.shirc.middleware.features.featuresservice;

import com.shirc.middleware.features.featuresfacade.SmsService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Author shirenchuang
 * @Date 2019/11/30 2:19 PM
 **/
@Service
@Component
public class SmsServiceImpl implements SmsService {


    @Override
    public String sendSms(String mobile, String content) {
        return String.format("发送结果: %s, 手机号: %s, 内容: %s,", "SUCCESS", mobile, content);
    }
}
