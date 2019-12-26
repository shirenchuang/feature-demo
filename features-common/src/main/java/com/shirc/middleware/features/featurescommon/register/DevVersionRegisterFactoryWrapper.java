package com.shirc.middleware.features.featurescommon.register;

import com.shirc.middleware.features.featurescommon.dev.MyThreadLocal;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.registry.Registry;
import org.apache.dubbo.registry.RegistryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author shirenchuang
 * RegistryFactory 的包装类，在注册的时候 修改一下 Application
 * 如果是 迭代环境则把Appliacation=Application_迭代版本号
 * @date 2019/12/5  8:29 下午
 */

public class DevVersionRegisterFactoryWrapper implements RegistryFactory {

    private static final Logger logger = LoggerFactory.getLogger("devVersion");


    private RegistryFactory registryFactory;
    /**
     * 注入RegisterFactory
     */
    public DevVersionRegisterFactoryWrapper(RegistryFactory registryFactory) {
        this.registryFactory = registryFactory;
    }

    @Override
    public Registry getRegistry(URL url) {
        //获取当前环境的迭代版本号
        if(!StringUtils.isEmpty(MyThreadLocal.localVersion)){
            logger.info("=====启动的服务是迭代版本服务  devVersion:{}=====",MyThreadLocal.localVersion);
            return new DevVersionRegisterWrapper(registryFactory.getRegistry(changeApplication(url)));
        }
        logger.info("=====启动的服务是稳定版本====");
        return registryFactory.getRegistry(url);
    }

    public static URL changeApplication(URL url){
        if(!StringUtils.isEmpty(MyThreadLocal.localVersion)){
            String applicationKey = url.getParameter(Constants.APPLICATION_KEY)+MyThreadLocal.spiltString+MyThreadLocal.localVersion;
            URL url2 = url.addParameter(Constants.APPLICATION_KEY,
                    applicationKey);

            logger.info("=====迭代版本服务修改 Application key：{} =====",applicationKey);
            return url2;
        }
        return url;
    }
}
