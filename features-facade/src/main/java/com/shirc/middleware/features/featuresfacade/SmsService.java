package com.shirc.middleware.features.featuresfacade;

/**
 * @Description TODO
 * @Author shirenchuang
 * @Date 2019/11/30 2:16 PM
 **/
public interface SmsService {

    String sendSms(String mobile, String content);

}
