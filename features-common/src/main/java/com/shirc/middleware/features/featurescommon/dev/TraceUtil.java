package com.shirc.middleware.features.featurescommon.dev;

import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * @Description
 * @Author shirenchuang
 * @Date 2019/12/10 9:23 PM
 **/
public class TraceUtil {

    //存的是别人过来的
    public static ThreadLocal<String> traceLocal = new ThreadLocal();


    public static String openTraceId = "false";

    static {
        openTraceId = System.getProperty("openTraceId");
    }


    public static String getTraceId(){
        String traceId = traceLocal.get();
        if(StringUtils.isEmpty(traceId)){
            return UUID.randomUUID().toString().replace("-","")+"_1";
        }else {
            String[] s = traceId.split("_");
            return s[0]+(Integer.parseInt(s[1])+1);
        }
    }

}
