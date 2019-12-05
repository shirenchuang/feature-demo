package com.shirc.middleware.features.featurescommon.dev;


import org.springframework.util.StringUtils;

/**
 * @author shirenchuang
 * @date 2019/12/4  2:52 下午
 */


public class MyThreadLocal {


    public static ThreadLocal<String> devVersion = new ThreadLocal();

    public static String localVersion  ;

    static {
        localVersion = System.getProperty("localVersion");
    }

    /**
     * 如果本地变量没有  则可能是第一个发起方;
     * 则去当前服务的版本号,然后一直传递下去;
     * @return
     */
    public static String getDevVersion(){
        String fromVersion = devVersion.get();
        if(!StringUtils.isEmpty(fromVersion)){
            return fromVersion;
        }
        return localVersion;
    }

}
