package com.shirc.middleware.features.featurescommon.dev;


import com.alibaba.ttl.TransmittableThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author shirenchuang
 * @date 2019/12/4  2:52 下午
 */


public class MyThreadLocal {
    private static final Logger logger = LoggerFactory.getLogger("devVersion");


    public static ThreadLocal<String> devVersion = new TransmittableThreadLocal();

    /**用户Application评价的固定字符;**/
    public static String spiltString = "_dev_";

    public static String localVersion  ;

    static {
        localVersion = System.getProperty("localVersion");
        logger.info("====devVersion:{}   ========",devVersion);
    }


    public static String getFromVersion(){
        return devVersion.get();
    }

    /**
     * 如果本地变量没有  则可能是第一个发起方;
     * 则去当前服务的版本号,然后一直传递下去;
     * @return
     */
    public static String getDevVersion(){
        String fromVersion = getFromVersion();
        if(!StringUtils.isEmpty(fromVersion)){
            return fromVersion;
        }
        return localVersion;
    }

}
