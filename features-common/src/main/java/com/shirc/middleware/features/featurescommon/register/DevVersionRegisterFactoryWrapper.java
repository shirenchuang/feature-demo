package com.shirc.middleware.features.featurescommon.register;

import com.shirc.middleware.features.featurescommon.dev.MyThreadLocal;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.registry.Registry;
import org.apache.dubbo.registry.RegistryFactory;
import org.springframework.util.StringUtils;

/**
 * @author shirenchuang
 * RegistryFactory 的包装类，在注册的时候 修改一下 Application
 * 如果是 迭代环境则把Appliacation=Application_迭代版本号
 * @date 2019/12/5  8:29 下午
 */

public class DevVersionRegisterFactoryWrapper implements RegistryFactory {


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
            return new DevVersionRegisterWrapper(registryFactory.getRegistry(changeApplication(url)));
        }
        return registryFactory.getRegistry(url);
    }

    public static URL changeApplication(URL url){
        if(!StringUtils.isEmpty(MyThreadLocal.localVersion)){
            URL url2 = url.addParameter(Constants.APPLICATION_KEY,
                    url.getParameter(Constants.APPLICATION_KEY)+"_"+MyThreadLocal.localVersion);
            return url2;
        }
        return url;
    }
}
